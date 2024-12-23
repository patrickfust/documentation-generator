package dk.fust.docgen.model.datadict;

import dk.fust.docgen.model.Description;
import lombok.Data;

import java.util.List;

/**
 * Data Dictionary File
 */
@Data
public class DataDictionaryFile {

    @Description(value = "Filename for the data dictionary", required = true)
    private String fileName;

    @Description(value = "Corresponding table name")
    private String tableName;

    @Description(value = "Data dictionary version", required = true)
    private String version;

    @Description("Description of the file")
    private String fileDescription;

    @Description(value = "The files columns", required = true)
    private List<Column> columns;

    @Description("List of tags that may be used in a filter")
    private List<String> tags;

}
