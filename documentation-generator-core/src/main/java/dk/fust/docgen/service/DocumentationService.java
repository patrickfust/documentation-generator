package dk.fust.docgen.service;

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
        ObjectMapper objectMapper = createJsonBuilder(file)
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        return objectMapper.readValue(file, Documentation.class);
    }

    private JsonMapper.Builder createJsonBuilder(File file) {
        if (file.getName().endsWith(".yaml") || file.getName().endsWith(".yml")) {
            return JsonMapper.builder(new YAMLFactory());
        }
        return JsonMapper.builder();
    }
}
