package dk.fust.docgen.excel.format.table;

import dk.fust.docgen.excel.format.table.model.CellStyleId;
import dk.fust.docgen.excel.format.table.model.ColumnCustomization;
import dk.fust.docgen.excel.format.table.model.ExcelConfiguration;
import dk.fust.docgen.excel.format.table.model.ExcelStyle;
import dk.fust.docgen.excel.format.table.model.ExcelStyles;
import dk.fust.docgen.format.table.Cell;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.format.table.Row;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts a FormatTable into an Excel Workbook
 */
@Slf4j
public class FormatTableToExcel {

    /**
     * Convert FormatTable to Excel
     * @param formatTable table to convert
     * @param excelConfiguration configuration
     * @return an instance of an Excel workbook
     */
    public static XSSFWorkbook toExcel(FormatTable formatTable, ExcelConfiguration excelConfiguration) {
        log.debug("Formatting table as Excel");
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = createSheet(workbook, excelConfiguration);
        Map<CellStyleId, CellStyle> styles = createStyles(workbook, excelConfiguration.getExcelStyles());
        int rowIdx = 0;
        int maxCol = 0;
        boolean hasShownHeader = false;
        for (Row row : formatTable.getRows()) {
            XSSFRow xssfRow = sheet.createRow(rowIdx);
            CellStyleId headerCellStyleId = hasShownHeader ? CellStyleId.SECONDARY_HEADER : CellStyleId.HEADER;
            int colIdx = 0;
            for (Cell cell : row.getCells()) {
                XSSFCell xssfCell = xssfRow.createCell(colIdx);
                if (cell != null) {
                    log.debug("cell: {}", cell);
                    hasShownHeader |= setCellStyle(cell, xssfCell, styles, headerCellStyleId, rowIdx);
                    setCellValue(cell, xssfCell);
                    if (cell.getColspan() > 1) {
                        colIdx += span(cell, rowIdx, colIdx, sheet);
                    }
                }
                maxCol = Math.max(maxCol, colIdx);
                colIdx++;
            }
            rowIdx++;
        }
        setColumnWidthsAndAutoResize(maxCol, sheet, excelConfiguration, formatTable);
        if (excelConfiguration.isAutofilter()) {
            setAutoFilter(sheet, maxCol, rowIdx);
        }
        return workbook;
    }

    private static void setAutoFilter(XSSFSheet sheet, int maxCol, int maxRow) {
        sheet.setAutoFilter(new CellRangeAddress(0, maxRow, 0, maxCol));
    }

    private static XSSFSheet createSheet(XSSFWorkbook workbook, ExcelConfiguration excelConfiguration) {
        XSSFSheet sheet = workbook.createSheet();
        sheet.setActiveCell(new CellAddress(0, 0));
        sheet.setDefaultColumnWidth(excelConfiguration.getDefaultColumnWidth());
        return sheet;
    }

    /**
     * @return if it's a header style or not
     */
    private static boolean setCellStyle(Cell cell, XSSFCell xssfCell, Map<CellStyleId, CellStyle> styles, CellStyleId headerCellStyleId, int rowIdx) {
        if (cell.isHeader()) {
            xssfCell.setCellStyle(styles.get(headerCellStyleId));
            return true;
        } else {
            xssfCell.setCellStyle(rowIdx % 2 == 0 ? styles.get(CellStyleId.EVEN_ROW) : styles.get(CellStyleId.ODD_ROW));
            return false;
        }
    }

    private static void setCellValue(Cell cell, XSSFCell xssfCell) {
        if (cell.getContentLong() != null) {
            xssfCell.setCellValue(cell.getContentLong());
        } else {
            xssfCell.setCellValue(cell.getContent());
        }
    }

    private static int span(Cell cell, int rowIdx, int colIdx, XSSFSheet sheet) {
        int colSpan = cell.getColspan() > 1 ? cell.getColspan() - 1 : 0;

        int firstRow = rowIdx;
        int lastRow = rowIdx;
        int firstCol = colIdx;
        int lastCol = colIdx + colSpan;

        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
        return colIdx + colSpan;
    }

    private static void setColumnWidthsAndAutoResize(int maxCol, XSSFSheet sheet, ExcelConfiguration excelConfiguration, FormatTable formatTable) {
        log.debug("Setting Column withs - Max cell width: {}, {}", maxCol, excelConfiguration.getColumnCustomizations());
        for (ColumnCustomization columnCustomization : excelConfiguration.getColumnCustomizations()) {
            int colIdx;
            if (columnCustomization.getColumnNumber() != null) {
                colIdx = columnCustomization.getColumnNumber();
            } else {
                colIdx = findIndexOfFirstColumnWithContent(formatTable, columnCustomization.getColumnDescription());
            }
            if (colIdx <= maxCol) {
                if (columnCustomization.getColumnWidth() != null) {
                    log.debug("Setting column width for col {} to width {}", columnCustomization.getColumnNumber(), (columnCustomization.getColumnWidth() * 256));
                    sheet.setColumnWidth(colIdx, columnCustomization.getColumnWidth() * 256);
                }
                if (columnCustomization.getAutoResize() != null) {
                    log.debug("Setting auto-resize on column {}", colIdx);
                    sheet.autoSizeColumn(colIdx);
                }
            }
        }
    }

    private static int findIndexOfFirstColumnWithContent(FormatTable formatTable, String content) {
        Assert.isTrue(formatTable.getRows() != null && !formatTable.getRows().isEmpty(), "Table must have rows");
        Row firstRow = formatTable.getRows().get(0);
        int colIdx = 0;
        for (Cell cell : firstRow.getCells()) {
            if (cell != null && cell.getContent() != null && cell.getContent().equals(content)) {
                return colIdx;
            }
            colIdx++;
        }
        throw new IllegalArgumentException("No cell with content '%s' found".formatted(content));
    }

    /**
     * cell styles used for formatting sheets
     */
    private static Map<CellStyleId, CellStyle> createStyles(Workbook wb, ExcelStyles excelStyles) {
        Map<CellStyleId, CellStyle> styles = new HashMap<>();

        styles.put(CellStyleId.HEADER, createCellStyle(excelStyles.getHeaderExcelStyle(), wb));
        styles.put(CellStyleId.SECONDARY_HEADER, createCellStyle(excelStyles.getSecondaryHeaderExcelStyle(), wb));
        styles.put(CellStyleId.ODD_ROW, createCellStyle(excelStyles.getOddRowExcelStyle(), wb));
        styles.put(CellStyleId.EVEN_ROW, createCellStyle(excelStyles.getEvenRowExcelStyle(), wb));

        return styles;
    }

    private static CellStyle createCellStyle(ExcelStyle excelStyle, Workbook wb) {
        Font font = wb.createFont();
        font.setFontHeightInPoints(excelStyle.getFontHeightInPoints());
        font.setFontName(excelStyle.getFontName());
        Assert.isNotNull(excelStyle.getFontColor().getIndexedColor(), "Font color can only handle named colors: WHITE, BLACK and so forth");
        font.setColor(excelStyle.getFontColor().getIndexedColor().getIndex());
        font.setBold(excelStyle.isBold());
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
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

    private static void setBorder(CellStyle cellStyle, ExcelStyle excelStyle) {
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
