package dk.fust.docgen;

import dk.fust.docgen.destination.Destination;

import java.io.File;

/**
 * This interface is the one that the generators will use to get the desired configuration.
 */
public interface GeneratorConfiguration {

    /**
     * Validates that the configuration are in order
     */
    void validate();

    /**
     * Location of the documentation file
     * @return Location of the documentation file
     */
    File getDocumentationFile();

    /**
     * Returns the specific implementation of a generator that can fulfill this configuration
     * @return the instance of the generator
     */
    Generator getGenerator();

    /**
     * Where to send the documentation
     * @return Where to send the documentation
     */
    Destination getDestination();

}
