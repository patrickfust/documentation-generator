package dk.fust.docgen.format.table;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Converts from model of a Table to a JSON or Yaml representation
 */
@Data
public class JsonTableFormatter implements TableFormatter {

    /**
     * Should it be yaml (false is json)
     */
    private boolean yaml = false;

    /**
     * Should the json be pretty printet or compressed
     */
    private boolean prettyPrint = true;

    @Override
    public String formatTable(FormatTable formatTable) {
        try {
            ObjectMapper objectMapper = createObjectMapper(yaml);
            List<Map<String, String>> json = tableToJson(formatTable);
            if (prettyPrint) {
                return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
            } else {
                return objectMapper.writeValueAsString(json);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private List<Map<String, String>> tableToJson(FormatTable formatTable) {
        List<Map<String, String>> result = new ArrayList<>();

        Row headerRow = null;
        for (Row row : formatTable.getRows()) {
            // First row is the header
            if (headerRow == null) {
                headerRow = row;
            } else {
                Map<String, String> rowMap = new HashMap<>();
                for (int cellIdx = 0; cellIdx < headerRow.getCells().size(); cellIdx++) {
                    if (row.getCells().size() > cellIdx) {
                        String key = String.valueOf(headerRow.getCells().get(cellIdx).getContent());
                        String value = row.getCells().get(cellIdx).getContent();
                        rowMap.put(key, value);
                    }
                }
                result.add(rowMap);
            }
        }
        return result;
    }

    private ObjectMapper createObjectMapper(boolean yaml) {
        JsonMapper.Builder builder;
        if (yaml) {
            builder = JsonMapper.builder(new YAMLFactory()
                    .disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER));
        } else {
            builder = JsonMapper.builder();
        }
        return builder.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS).build();
    }
}
