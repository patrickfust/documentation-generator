package dk.fust.docgen;

import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.service.DocumentationService;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * Main class - Everything starts here
 * This class loops over all the configurations and run their generators
 */
@Slf4j
public class DocumentationGenerator {

    private final DocumentationService documentationService = new DocumentationService();

    /**
     * Loops over all the configurations and generates the documentation
     * @param generatorConfigurations list of configurations to run
     */
    public void generate(List<GeneratorConfiguration> generatorConfigurations) {
        Assert.isNotNull(generatorConfigurations, "DocumentationGenerator: documentationConfigurations must not be null");
        generatorConfigurations.forEach(this::generate);
    }

    /**
     * Generates documentation for a single configuration
     * @param generatorConfiguration configuration to generate documentation for
     */
    public void generate(GeneratorConfiguration generatorConfiguration) {
        Assert.isNotNull(generatorConfiguration, "DocumentationGenerator: Missing configuration");
        generatorConfiguration.validate();
        try {
            Documentation documentation = documentationService.loadDocumentation(generatorConfiguration.getDocumentationFile());
            validateModel(documentation);
            Generator generator = generatorConfiguration.getGenerator();
            generator.generate(documentation, generatorConfiguration);
        } catch (IOException e) {
            log.error("Could not generate documentation", e);
            throw new RuntimeException(e);
        }
    }

    private void validateModel(Documentation documentation) {
        log.info("Validating documentation...");
        ModelValidator modelValidator = new ModelValidator(documentation);
        modelValidator.validate();
    }

}
