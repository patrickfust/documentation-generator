package dk.fust.docgen.excel.format.table.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Configuration for generating Excel workbooks
 */
@Data
public class ExcelConfiguration {

    private ExcelStyles excelStyles;

    /**
     * Sets the default column width
     */
    private int defaultColumnWidth;

    /**
     * After setting all cells, these columns will call autoResize
     */
    private List<Integer> autoResizeColumns = new ArrayList<>();

}
