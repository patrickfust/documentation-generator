package dk.fust.docgen.maven;

import dk.fust.docgen.DocumentationGenerator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.service.DocumentationConfigurationLoaderService;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Maven plugin for documentation generator
 */
@Mojo(name = "generateDocumentation", defaultPhase = LifecyclePhase.NONE)
public class DocumentationGeneratorMojo extends AbstractMojo {

    @Parameter(property = "documentationConfigurationFile")
    private File documentationConfigurationFile;

    @Override
    public void execute() {
        getLog().info("Parsing documentationConfigurationFile");
        try {
            DocumentationConfigurationLoaderService documentationConfigurationLoaderService = new DocumentationConfigurationLoaderService();
            List<GeneratorConfiguration> confs = documentationConfigurationLoaderService.readConfigurations(documentationConfigurationFile);
            DocumentationGenerator documentationGenerator = new DocumentationGenerator();
            documentationGenerator.generate(confs);
        } catch (IOException e) {
            throw new RuntimeException("Can't load generator configurations", e);
        }
    }
}
