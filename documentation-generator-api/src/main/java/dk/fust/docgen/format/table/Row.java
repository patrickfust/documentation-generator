package dk.fust.docgen.format.table;

import lombok.Data;

import java.util.List;

/**
 * Represent a row in a table
 */
@Data
public class Row {

    private List<Cell> cells;

}
