package dk.fust.docgen.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import dk.fust.docgen.model.Documentation;

import java.io.File;
import java.io.IOException;

/**
 * Service to read documentation files
 */
public class DocumentationService {

    /**
     * Read the file and parse it.
     * Handles JSON and YAML
     * @param file documentation file
     * @return the parsed document
     * @throws IOException an error occurred
     */
    public Documentation loadDocumentation(File file) throws IOException {
        ObjectMapper objectMapper = createObjectMapper(file);
        return objectMapper.readValue(file, Documentation.class);
    }

    /**
     * Reads the contents of a file into JsonNodes
     * @param file file to read
     * @return parsed Json or yaml
     * @throws IOException an error occurred
     */
    public JsonNode loadFileAsTree(File file) throws IOException {
        ObjectMapper objectMapper = createObjectMapper(file);
        return objectMapper.readTree(file);
    }

    private ObjectMapper createObjectMapper(File file) {
        JsonMapper.Builder builder;
        if (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml")) {
            builder = JsonMapper.builder(new YAMLFactory());
        } else {
            builder = JsonMapper.builder();
        }
        return builder.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build();
    }
}
