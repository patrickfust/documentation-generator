package dk.fust.docgen.destination;

import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Can write the entire document to the specified file
 */
@Data
public class FileDestination implements Destination {

    private File file;

    @Override
    public void validate() {
        Assert.isNotNull(file, "file is required");
    }

    @Override
    public void sendDocumentToDestination(String document, String destination) throws IOException {
        Files.writeString(file.toPath(), document);
    }

}
