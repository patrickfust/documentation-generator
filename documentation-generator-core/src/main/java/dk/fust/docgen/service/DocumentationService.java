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
     * Read the file and parse it
     * @param file documetation file
     * @return the parsed document
     * @throws IOException an error occurred
     */
    public Documentation loadDocumentation(File file) throws IOException {
        ObjectMapper objectMapper = JsonMapper.builder(new YAMLFactory())
                .enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS)
                .build();
        return objectMapper.readValue(file, Documentation.class);
    }

}
