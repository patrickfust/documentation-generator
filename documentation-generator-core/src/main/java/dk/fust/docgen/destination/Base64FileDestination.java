package dk.fust.docgen.destination;

import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;

/**
 * Can write the entire document base64 decoded to the specified file.
 * This way you can write binary files
 */
@Data
public class Base64FileDestination implements Destination {

    private File file;

    @Override
    public void validate() {
        Assert.isNotNull(file, "file is required");
    }

    @Override
    public void sendDocumentToDestination(String document, String destination) throws IOException {
        Files.write(file.toPath(), Base64.getDecoder().decode(document));
    }

}
