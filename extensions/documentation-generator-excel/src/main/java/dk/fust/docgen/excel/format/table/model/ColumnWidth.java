package dk.fust.docgen.excel.format.table.model;

import lombok.Data;

/**
 * Definition of a column number and it's width (approx. number of characters).
 * First column is number 0.
 */
@Data
public class ColumnWidth {

    private int width;

    private int columnNumber;

}
