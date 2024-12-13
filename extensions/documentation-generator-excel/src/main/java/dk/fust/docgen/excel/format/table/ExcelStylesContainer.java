package dk.fust.docgen.excel.format.table;

import dk.fust.docgen.excel.format.table.model.CellStyleId;
import dk.fust.docgen.excel.format.table.model.ExcelStyle;
import dk.fust.docgen.excel.format.table.model.ExcelStyles;
import dk.fust.docgen.format.table.Alignment;
import dk.fust.docgen.util.Assert;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Container for cell styles used for formatting sheets
 */
public class ExcelStylesContainer {

    private final Map<CellStyleId, CellStyle> stylesLeftAligned;
    private final Map<CellStyleId, CellStyle> stylesCenterAligned;
    private final Map<CellStyleId, CellStyle> stylesRightAligned;

    /**
     * Cell styles used for formatting sheets
     * @param workbook workbook to create styles in
     * @param excelStyles styles to use
     */
    public ExcelStylesContainer(Workbook workbook, ExcelStyles excelStyles) {
        stylesLeftAligned = createStyles(workbook, excelStyles, Alignment.LEFT);
        stylesRightAligned = createStyles(workbook, excelStyles, Alignment.RIGHT);
        stylesCenterAligned = createStyles(workbook, excelStyles, Alignment.CENTER);
    }

    /**
     * Returns the cell style to use for a given cell style id and alignment
     * @param cellStyleId cell style id
     * @param alignment alignment
     * @return cell style
     */
    public CellStyle get(CellStyleId cellStyleId, Alignment alignment) {
        if (alignment == null) {
            return stylesLeftAligned.get(cellStyleId);
        }
        return switch (alignment) {
            case LEFT -> stylesLeftAligned.get(cellStyleId);
            case RIGHT -> stylesRightAligned.get(cellStyleId);
            case CENTER -> stylesCenterAligned.get(cellStyleId);
        };
    }

    private Map<CellStyleId, CellStyle> createStyles(Workbook wb, ExcelStyles excelStyles, Alignment alignment) {
        Map<CellStyleId, CellStyle> styles = new HashMap<>();

        styles.put(CellStyleId.HEADER, createCellStyle(excelStyles.getHeaderExcelStyle(), wb, alignment));
        styles.put(CellStyleId.SECONDARY_HEADER, createCellStyle(excelStyles.getSecondaryHeaderExcelStyle(), wb, alignment));
        styles.put(CellStyleId.ODD_ROW, createCellStyle(excelStyles.getOddRowExcelStyle(), wb, alignment));
        styles.put(CellStyleId.EVEN_ROW, createCellStyle(excelStyles.getEvenRowExcelStyle(), wb, alignment));

        return styles;
    }

    private CellStyle createCellStyle(ExcelStyle excelStyle, Workbook wb, Alignment alignment) {
        Font font = wb.createFont();
        font.setFontHeightInPoints(excelStyle.getFontHeightInPoints());
        font.setFontName(excelStyle.getFontName());
        Assert.isNotNull(excelStyle.getFontColor().getIndexedColor(), "Font color can only handle named colors: WHITE, BLACK and so forth");
        font.setColor(excelStyle.getFontColor().getIndexedColor().getIndex());
        font.setBold(excelStyle.isBold());
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(toHorizontalAlignment(alignment));
        if (excelStyle.getBackgroundColor().getIndexedColor() != null) {
            cellStyle.setFillForegroundColor(excelStyle.getBackgroundColor().getIndexedColor().getIndex());
        } else {
            cellStyle.setFillForegroundColor(excelStyle.getBackgroundColor().getXssfColor());
        }
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        cellStyle.setWrapText(true);
        setBorder(cellStyle, excelStyle);
        return cellStyle;
    }

    private HorizontalAlignment toHorizontalAlignment(Alignment alignment) {
        return switch (alignment) {
            case LEFT -> HorizontalAlignment.LEFT;
            case RIGHT -> HorizontalAlignment.RIGHT;
            case CENTER -> HorizontalAlignment.CENTER;
        };
    }

    private void setBorder(CellStyle cellStyle, ExcelStyle excelStyle) {
        if (excelStyle.getBorderColor() != null && excelStyle.getBorderColor().getIndexedColor() != null) {
            cellStyle.setBorderBottom(BorderStyle.THIN);
            cellStyle.setBorderLeft(BorderStyle.THIN);
            cellStyle.setBorderRight(BorderStyle.THIN);
            cellStyle.setBorderTop(BorderStyle.THIN);
            short borderColorIndex = excelStyle.getBorderColor().getIndexedColor().getIndex();
            cellStyle.setBottomBorderColor(borderColorIndex);
            cellStyle.setLeftBorderColor(borderColorIndex);
            cellStyle.setRightBorderColor(borderColorIndex);
            cellStyle.setTopBorderColor(borderColorIndex);
        }
    }

}
