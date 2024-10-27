package dk.fust.docgen.gradle;

import dk.fust.docgen.GeneratorConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent the configuration possibilities
 */
public class DocumentationGeneratorPluginExtension {

    private File documentationConfigurationFile;
    private List<GeneratorConfiguration> generatorConfigurations = new ArrayList<>();

    /**
     * Retrieves the configurations
     * @return the configurations
     */
    public List<GeneratorConfiguration> getGeneratorConfigurations() {
        return generatorConfigurations;
    }

    /**
     * Sets the configurations
     * Can't be together with 'documentationConfigurationFile'
     * @param generatorConfigurations the configurations
     */
    public void setGeneratorConfigurations(List<GeneratorConfiguration> generatorConfigurations) {
        this.generatorConfigurations = generatorConfigurations;
    }

    /**
     * Retrieves the configuration file
     * @return configuration file
     */
    public File getDocumentationConfigurationFile() {
        return documentationConfigurationFile;
    }

    /**
     * Sets the yaml- or json-file that contains the configuration.
     * Can't be together with 'generatorConfigurations'
     * @param documentationConfigurationFile file for documentation configurations
     */
    public void setDocumentationConfigurationFile(File documentationConfigurationFile) {
        this.documentationConfigurationFile = documentationConfigurationFile;
    }
}
