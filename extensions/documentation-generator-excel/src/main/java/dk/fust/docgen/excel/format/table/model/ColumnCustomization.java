package dk.fust.docgen.excel.format.table.model;

import lombok.Data;

/**
 * Class that holds the customization for a column in a table
 */
@Data
public class ColumnCustomization {

    private Integer columnNumber;
    private String columnDescription;
    private Boolean autoResize;
    private Integer columnWidth;

}
