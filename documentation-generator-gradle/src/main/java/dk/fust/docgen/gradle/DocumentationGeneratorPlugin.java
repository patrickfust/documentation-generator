package dk.fust.docgen.gradle;

import dk.fust.docgen.DocumentationGenerator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.service.DocumentationConfigurationLoaderService;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;

/**
 * Generates documentation based upon a single-source-of-truth yaml file.
 */
public class DocumentationGeneratorPlugin implements Plugin<Project> {
    private static final Logger log = LoggerFactory.getLogger(DocumentationGeneratorPlugin.class);

    @Override
    public void apply(Project project) {
        DocumentationGeneratorPluginExtension extension = project.getExtensions().create("documentationGenerator", DocumentationGeneratorPluginExtension.class);

        Task generateDocumentationTask = project.task("generateDocumentation").doLast(task -> {
            log.info("Generating documentation...");
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
                    List<GeneratorConfiguration> confs = documentationConfigurationLoaderService.readConfigurations(extension.getDocumentationConfigurationFile());
                    documentationGenerator.generate(confs);
                } catch (IOException e) {
                    throw new RuntimeException("Can't load generator configurations", e);
                }
            }
        });
        generateDocumentationTask.setDescription("Generates documentation");
        generateDocumentationTask.setGroup("documentation");
    }
}
