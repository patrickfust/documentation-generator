package dk.fust.docgen.model.datadict;

import dk.fust.docgen.model.Description;
import lombok.Data;

/**
 * Data dictionary column
 */
@Data
public class Column {

    @Description(value = "Column name in file", required = true)
    private String columnName;

    @Description("Description of the field. May be several lines")
    private String columnDescription;

    @Description(value = "Columns data type. Examples: varchar, varchar(200), numeric(17, 3)", required = true)
    private String dataType;

    @Description("Regular expression that the contain must obey. Example: ((\\d{3}[A-Z]?)|0{4})")
    private String regex;

    @Description("Example on how the content may look. Could be a description as well: 'value between 0 and 1. eg 0,3333'")
    private String example;

    @Description(value = "If true, then the content may not be null or empty", defaultBoolean = false)
    private Boolean mandatory = Boolean.FALSE;

    @Description("Minimum value of the content")
    private Long minimumValue;

    @Description("Maximum value of the content")
    private Long maximumValue;

    @Description(value = "Is this column a part of the unique key?")
    private String keys;
}
