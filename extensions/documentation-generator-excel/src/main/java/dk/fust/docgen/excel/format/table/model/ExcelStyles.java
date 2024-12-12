package dk.fust.docgen.excel.format.table.model;

import lombok.Data;

/**
 * Collection of ExcelStyle
 */
@Data
public class ExcelStyles {

    private ExcelStyle headerExcelStyle;
    private ExcelStyle secondaryHeaderExcelStyle;
    private ExcelStyle oddRowExcelStyle;
    private ExcelStyle evenRowExcelStyle;

    /**
     * Constructor with all available styles
     * @param headerExcelStyle style for header
     * @param secondaryHeaderExcelStyle style for secondary headers
     * @param evenRowExcelStyle style for even rows
     * @param oddRowExcelStyle style for odd rows
     */
    public ExcelStyles(ExcelStyle headerExcelStyle, ExcelStyle secondaryHeaderExcelStyle, ExcelStyle oddRowExcelStyle, ExcelStyle evenRowExcelStyle) {
        this.headerExcelStyle = headerExcelStyle;
        this.secondaryHeaderExcelStyle = secondaryHeaderExcelStyle;
        this.oddRowExcelStyle = oddRowExcelStyle;
        this.evenRowExcelStyle = evenRowExcelStyle;
    }

}
