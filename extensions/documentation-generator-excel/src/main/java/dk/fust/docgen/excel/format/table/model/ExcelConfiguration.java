package dk.fust.docgen.excel.format.table.model;

import dk.fust.docgen.util.Assert;
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

    private boolean autofilter;

    private List<ColumnCustomization> columnCustomizations = new ArrayList<>();

    /**
     * Validates the configuration
     */
    public void validate() {
        if (columnCustomizations != null) {
            for (ColumnCustomization columnCustomization : columnCustomizations) {
                Assert.isFalse(columnCustomization.getColumnNumber() == null && columnCustomization.getColumnDescription() == null, "Either columnNumber or columnDescription must be set");
                if (columnCustomization.getColumnNumber() != null) {
                    Assert.isTrue(columnCustomization.getColumnNumber() >= 0, "Column number must be greater than or equal to 0");
                    Assert.isNull(columnCustomization.getColumnDescription(), "Column description must not be set when column number is set");
                } else {
                    Assert.isTrue(!columnCustomization.getColumnDescription().isEmpty(), "Column description must be set");
                }
                Assert.isFalse(columnCustomization.getColumnWidth() != null && columnCustomization.getAutoResize() != null, "Can't have both width and autoResize set");
            }
        }
    }
}
