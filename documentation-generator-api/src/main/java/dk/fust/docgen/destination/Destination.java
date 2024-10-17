package dk.fust.docgen.destination;

import java.io.IOException;

/**
 * This interface describes a way to send th the generatet
 * documentation to the destination.
 * It could be a file, markdown, confluence or something completely different.
 */
public interface Destination {

    /**
     * Is everything all-right?
     */
    void validate();

    /**
     * Send the generated document to the destination
     * @param document document to send
     * @param destination where the document should be sent to
     * @throws IOException error occurred
     */
    void sendDocumentToDestination(String document, String destination) throws IOException;

}
