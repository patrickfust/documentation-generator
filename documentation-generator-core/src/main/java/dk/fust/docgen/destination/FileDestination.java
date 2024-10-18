package dk.fust.docgen.destination;

import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Can write documents to files
 */
@Data
public class FileDestination implements Destination {

    private File directory;
    private boolean createParentDirectories = false;

    /**
     * Validate that the configuration are in order
     */
    @Override
    public void validate() {
        Assert.isNotNull(directory, "directory is required");
        if (!directory.exists() && createParentDirectories) {
            Assert.isTrue(directory.mkdirs(), "Can't create parent directories");
        }
        Assert.isTrue(directory.exists(), "Directory does not exist");
        Assert.isTrue(directory.isDirectory(), "Directory is not a directory");
    }

    /**
     * Write to the file
     * @param document document to write to the file
     * @param destination filename in the directory
     * @throws IOException an error occurred
     */
    @Override
    public void sendDocumentToDestination(String document, String destination) throws IOException {
        File file = new File(directory, destination);
        Files.writeString(file.toPath(), document);
    }

}
