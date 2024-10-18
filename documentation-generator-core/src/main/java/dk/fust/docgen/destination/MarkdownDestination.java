package dk.fust.docgen.destination;

import dk.fust.docgen.util.Assert;
import dk.fust.docgen.service.FileService;
import lombok.Data;

import java.io.File;
import java.io.IOException;

/**
 * Replaces part of a Markdown file
 */
@Data
public class MarkdownDestination implements Destination {
    private File file;

    /**
     * Uses regex to replace parts of the Markdown file
     * @param document part to replace the existing content
     * @param destination regex to find the part in then Markdownfile
     * @throws IOException an error occurred
     */
    @Override
    public void sendDocumentToDestination(String document, String destination) throws IOException {
        String regex = "\\[//]: #%s_START \\(\\).*\\[//]: #%s_END \\(\\)".formatted(destination, destination);
        String replacement = """
            [//]: #%s_START ()
            %s[//]: #%s_END ()""".formatted(destination, document, destination);
        FileService fileService = new FileService();
        fileService.replaceInFile(file, regex, replacement);
    }

    /**
     * Validate that the configuration are in order
     */
    @Override
    public void validate() {
        Assert.isNotNull(file, "MarkdownDestination: file must not be null");
        Assert.isTrue(file.exists(), "MarkdownDestination: file does not exist: " + file);
        Assert.isFalse(file.isDirectory(), "MarkdownDestination: file is a directory: " + file);
    }
}
