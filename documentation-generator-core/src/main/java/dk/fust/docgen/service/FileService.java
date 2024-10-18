package dk.fust.docgen.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper service for writing parts of files
 */
public class FileService {

    /**
     * Uses regex to substitute part of a file
     * @param file file to read and write
     * @param regex what to search for
     * @param replacement the content where the regex is found
     * @throws IOException an error occurred
     */
    public void replaceInFile(File file, String regex, String replacement) throws IOException {
        StringBuilder builder = new StringBuilder();
        Path path = file.toPath();
        String content = Files.readString(path);
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            matcher.appendReplacement(builder, Matcher.quoteReplacement(replacement));
        }
        matcher.appendTail(builder);
        Files.write(path, builder.toString().getBytes());
    }

}
