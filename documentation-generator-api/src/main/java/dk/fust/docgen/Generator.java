package dk.fust.docgen;

import dk.fust.docgen.model.Documentation;

import java.io.IOException;

/**
 * Every generator must implement this.
 * This will then be called from the framework.
 */
public interface Generator {

    /**
     * Generates documentation for the model
     * @param documentation model to generate documentation for
     * @param generatorConfiguration how it should generate
     * @throws IOException if an error occurs
     */
    void generate(Documentation documentation, GeneratorConfiguration generatorConfiguration) throws IOException;

}
