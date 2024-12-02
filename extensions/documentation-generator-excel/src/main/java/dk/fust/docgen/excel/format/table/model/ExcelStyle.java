package dk.fust.docgen.excel.format.table.model;

import lombok.Data;

/**
 * A definition of an style for a cell in Excel
 */
@Data
public class ExcelStyle {

    private String fontName;
    private short fontHeightInPoints;
    private ExcelColor fontColor;
    private boolean bold = true;
    private ExcelColor backgroundColor;

    /**
     * Default empty constructor
     */
    public ExcelStyle() {
    }

    /**
     * Constructor with all available fields
     * @param fontName font name
     * @param fontHeightInPoints height in point
     * @param bold bold or not
     * @param fontColor font color
     * @param backgroundColor background color
     */
    public ExcelStyle(String fontName, short fontHeightInPoints, boolean bold, ExcelColor fontColor, ExcelColor backgroundColor) {
        this.fontName = fontName;
        this.fontHeightInPoints = fontHeightInPoints;
        this.fontColor = fontColor;
        this.bold = bold;
        this.backgroundColor = backgroundColor;
    }

}
