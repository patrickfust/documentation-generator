package dk.fust.docgen.format.table;

import lombok.Data;

/**
 * Represent a cell in a table
 */
@Data
public class Cell {

    private int colspan = 1;
    private int rowspan = 1;
    private boolean header = false;
    private String content;

}
