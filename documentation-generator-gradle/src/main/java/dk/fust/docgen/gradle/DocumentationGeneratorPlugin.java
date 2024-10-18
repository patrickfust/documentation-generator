package dk.fust.docgen.gradle;

import dk.fust.docgen.DocumentationGenerator;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
            DocumentationGenerator documentationGenerator = new DocumentationGenerator();
            documentationGenerator.generate(extension.getGeneratorConfigurations());
        });
        generateDocumentationTask.setDescription("Generates documentation");
        generateDocumentationTask.setGroup("documentation");
    }
}
