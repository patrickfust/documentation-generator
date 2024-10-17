package dk.fust.docgen.format.table;

import lombok.Data;

import java.util.List;

/**
 * Main POJO for at table
 */
@Data
public class Table {

    private String tableClass;

    private List<Row> rows;

}
