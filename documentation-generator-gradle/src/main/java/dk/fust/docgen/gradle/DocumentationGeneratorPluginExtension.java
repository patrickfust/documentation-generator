package dk.fust.docgen.gradle;

import dk.fust.docgen.GeneratorConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that represent the configuration possibilities
 */
public class DocumentationGeneratorPluginExtension {

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
     * @param generatorConfigurations the configurations
     */
    public void setGeneratorConfigurations(List<GeneratorConfiguration> generatorConfigurations) {
        this.generatorConfigurations = generatorConfigurations;
    }
}
