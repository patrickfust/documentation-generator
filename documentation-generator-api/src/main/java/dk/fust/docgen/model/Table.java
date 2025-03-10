package dk.fust.docgen.model;

import lombok.Data;

import java.util.List;
import java.util.Objects;

/**
 * Definition of a database table
 */
@Data
public class Table {

    private String name;

    @Description("Comment to be added to the table")
    private String comment;

    @Description("List of tags that may be used in a filter")
    private List<String> tags;

    @Description(value = "Filename of sql-script-file to be generated. If left out, no files are generated")
    private String createTableScript;

    private List<Field> fields;

    private List<View> views;

    private List<Index> indexes;

    @Description("Configuration on how the generation should appear - Overrides the general generation on `Documentation`")
    private Generation generation;

    /**
     * Retrieves the found field. If it's a generated field, it's synthesized.
     * @param fieldName name to find
     * @param generatedIdDataType if it's a generated field, this will be the data type
     * @param documentation refernce to the complete model
     * @return found field or null if it don't exist
     */
    public Field getField(String fieldName, DataType generatedIdDataType, Documentation documentation) {
        Field foundField = fields.stream().filter(field -> field.getName().equals(fieldName)).findFirst().orElse(null);
        if (foundField == null) {
            boolean isGeneratedId = Objects.equals(fieldName, name + "_id");
            if (documentation.getGenerationForTable(this).isGenerateId() && isGeneratedId) {
                // Synthesized generated id field
                foundField = new Field();
                foundField.setName(fieldName);
                foundField.setDataType(generatedIdDataType);
            }
        }
        return foundField;
    }
}
