package dk.fust.docgen.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * Main class of the documentation.
 * Everything starts here
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Documentation {

    private String documentationTitle;

    @Description("Schema name")
    private String schemaName;
    private List<Table> tables = new ArrayList<>();

    @Description("Default configuration on how the generation should appear - Can be overridden per table")
    private Generation generation;

    /**
     * Retrieves a field matching table name and field aname
     * @param tableName tableName to search for
     * @param fieldName fieldName to search for
     * @param generateIdDataType if the field is a generated field, this will be the data type
     * @return the found field or null if not available
     */
    public Field getField(String tableName, String fieldName, DataType generateIdDataType) {
        return tables.stream().filter(t -> t.getName().equals(tableName))
                .map(t -> t.getField(fieldName, generateIdDataType, this))
                .map(Optional::ofNullable) // The findFirst will otherwise throw up if it gets a null
                .findFirst().flatMap(Function.identity())
                .orElse(null);
    }

    /**
     * What is the configuration for the specific table?
     * Configuration for the table wins over the general configuration
     * @param table table to examine
     * @return the tables configuration
     */
    public Generation getGenerationForTable(Table table) {
        Generation tableGeneration = new Generation();
        mergeGenerations(getGeneration(), tableGeneration);
        mergeGenerations(table.getGeneration(), tableGeneration);

        // Defaults
        if (tableGeneration.getGenerateIdDataType() == null) {
            tableGeneration.setGenerateIdDataType(DataType.INT);
        }
        if (tableGeneration.getColumnNameCreatedAt() == null) {
            tableGeneration.setColumnNameCreatedAt("created_at");
        }
        if (tableGeneration.getColumnNameUpdatedAt() == null) {
            tableGeneration.setColumnNameUpdatedAt("updated_at");
        }
        return tableGeneration;
    }

    private void mergeGenerations(Generation from, Generation to) {
        if (from != null) {
            if (from.getGenerateId() != null) {
                to.setGenerateId(from.getGenerateId());
            }
            if (from.getGenerateIdDataType() != null) {
                to.setGenerateIdDataType(from.getGenerateIdDataType());
            }
            if (from.getAddCreatedAt() != null) {
                to.setAddCreatedAt(from.getAddCreatedAt());
            }
            if (from.getColumnNameCreatedAt() != null && !from.getColumnNameCreatedAt().isEmpty()) {
                to.setColumnNameCreatedAt(from.getColumnNameCreatedAt());
            }
            if (from.getAddUpdatedAt() != null) {
                to.setAddUpdatedAt(from.getAddUpdatedAt());
            }
            if (from.getColumnNameUpdatedAt() != null && !from.getColumnNameUpdatedAt().isEmpty()) {
                to.setColumnNameUpdatedAt(from.getColumnNameUpdatedAt());
            }
            if (from.getTriggerForUpdates() != null && !from.getTriggerForUpdates().isEmpty()) {
                to.setTriggerForUpdates(from.getTriggerForUpdates());
            }
        }
    }
}
