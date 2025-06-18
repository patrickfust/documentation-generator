package dk.fust.docgen.gradle;

import dk.fust.docgen.DocumentationGenerator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.service.DocumentationConfigurationLoaderService;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Generates documentation based upon a single-source-of-truth yaml file.
 */
public class DocumentationGeneratorPlugin implements Plugin<Project> {
    private static final Logger log = LoggerFactory.getLogger(DocumentationGeneratorPlugin.class);

    @Override
    public void apply(Project project) {
        String projectDir = project.getProjectDir().getAbsolutePath();
        DocumentationGeneratorPluginExtension extension = project.getExtensions().create("documentationGenerator", DocumentationGeneratorPluginExtension.class);

        project.getTasks().register("generateDocumentation", task -> {
            task.setDescription("Generates documentation");
            task.setGroup("documentation");
            task.doLast(t -> {
                log.debug("Validating configuration...");
                if (extension.getGeneratorConfigurations() == null && extension.getDocumentationConfigurationFile() == null) {
                    throw new IllegalArgumentException("Need either GeneratorConfigurations or DocumentationConfigurationFile");
                }
                if (extension.getGeneratorConfigurations() != null && !extension.getGeneratorConfigurations().isEmpty() &&
                        extension.getDocumentationConfigurationFile() != null) {
                    throw new IllegalArgumentException("Can't have both GeneratorConfigurations and DocumentationConfigurationFile");
                }

                DocumentationGenerator documentationGenerator = new DocumentationGenerator();
                if (extension.getGeneratorConfigurations() != null && !extension.getGeneratorConfigurations().isEmpty()) {
                    documentationGenerator.generate(extension.getGeneratorConfigurations());
                } else {
                    DocumentationConfigurationLoaderService documentationConfigurationLoaderService = new DocumentationConfigurationLoaderService();
                    try {
                        List<GeneratorConfiguration> confs;
                        File documentationConfigurationFile = extension.getDocumentationConfigurationFile();
                        if (documentationConfigurationFile.exists()) {
                            log.info("Found configuration file: {}", documentationConfigurationFile.getPath());
                            confs = documentationConfigurationLoaderService.readConfigurations(documentationConfigurationFile);
                        } else {
                            File projectConfigurationFile = new File(projectDir, documentationConfigurationFile.getPath());
                            log.debug("Could not find configuration file -> trying i project dir: " + projectConfigurationFile.getAbsolutePath());
                            log.debug("Exists now: " + projectConfigurationFile.exists());
                            if (projectConfigurationFile.exists()) {
                                log.info("Found project configuration file: {}", projectConfigurationFile.getPath());
                            }
                            documentationGenerator.setBaseDir(new File(projectDir));
                            confs = documentationConfigurationLoaderService.readConfigurations(projectConfigurationFile);
                        }
                        log.debug("Calling generate...");
                        documentationGenerator.generate(confs);
                    } catch (IOException e) {
                        throw new RuntimeException("Can't load generator configurations", e);
                    }
                }
            });
        });
    }
}
