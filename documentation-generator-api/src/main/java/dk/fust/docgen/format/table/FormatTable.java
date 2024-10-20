package dk.fust.docgen.format.table;

import lombok.Data;

import java.util.List;

/**
 * Main POJO for at table to be formatted
 */
@Data
public class FormatTable {

    private String tableClass;

    private List<Row> rows;

    private ColGroup colGroup;
}
