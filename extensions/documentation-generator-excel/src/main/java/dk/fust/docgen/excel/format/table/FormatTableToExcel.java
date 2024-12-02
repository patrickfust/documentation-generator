package dk.fust.docgen.excel.format.table;

import dk.fust.docgen.excel.format.table.model.CellStyleId;
import dk.fust.docgen.excel.format.table.model.ColumnWidth;
import dk.fust.docgen.excel.format.table.model.ExcelConfiguration;
import dk.fust.docgen.excel.format.table.model.ExcelStyle;
import dk.fust.docgen.excel.format.table.model.ExcelStyles;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
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
        XSSFSheet sheet = workbook.createSheet();
        Map<CellStyleId, CellStyle> styles = createStyles(workbook, excelConfiguration.getExcelStyles());
        sheet.setActiveCell(new CellAddress(0, 0));
        sheet.setDefaultColumnWidth(excelConfiguration.getDefaultColumnWidth());
        int rowIdx = 0;
        int maxCol = 0;
        boolean hasShownHeader = false;
        for (dk.fust.docgen.format.table.Row fustRow : formatTable.getRows()) {
            XSSFRow row = sheet.createRow(rowIdx);
            int colIdx = 0;
            CellStyleId headerCellStyleId = hasShownHeader ? CellStyleId.SECONDARY_HEADER : CellStyleId.HEADER;
            for (dk.fust.docgen.format.table.Cell fustCell : fustRow.getCells()) {
                XSSFCell cell = row.createCell(colIdx);
                if (fustCell != null) {
                    if (fustCell.isHeader()) {
                        cell.setCellStyle(styles.get(headerCellStyleId));
                        hasShownHeader = true;
                    } else {
                        cell.setCellStyle(rowIdx % 2 == 0 ? styles.get(CellStyleId.EVEN_ROW) : styles.get(CellStyleId.ODD_ROW));
                    }
                    cell.setCellValue(fustCell.getContent());
                    if (fustCell.getColspan() > 1 || fustCell.getRowspan() > 1) {
                        int rowSpan = fustCell.getRowspan() > 1 ? fustCell.getRowspan() - 1 : 0;
                        int colSpan = fustCell.getColspan() > 1 ? fustCell.getColspan() - 1 : 0;

                        int firstRow = rowIdx;
                        int lastRow = rowIdx + rowSpan;
                        int firstCol = colIdx;
                        int lastCol = colIdx + colSpan;

                        sheet.addMergedRegion(new CellRangeAddress(firstRow, lastRow, firstCol, lastCol));
                        colIdx += colSpan;
                        rowIdx += rowSpan;
                    }
                }
                maxCol = Math.max(maxCol, colIdx);
                colIdx++;
            }
            rowIdx++;
        }
        autoResize(maxCol, sheet, excelConfiguration);
        setColumnWidths(maxCol, sheet, excelConfiguration);
        return workbook;
    }

    private static void setColumnWidths(int maxCol, XSSFSheet sheet, ExcelConfiguration excelConfiguration) {
        log.debug("Setting Column withs - Max cell width: {}, {}", maxCol, excelConfiguration.getColumnWidths());
        for (ColumnWidth columnWidth : excelConfiguration.getColumnWidths()) {
            if (columnWidth.getColumnNumber() <= maxCol) {
                log.debug("Setting column width for col {} to width {}", columnWidth.getColumnNumber(), (columnWidth.getWidth() * 256));
                sheet.setColumnWidth(columnWidth.getColumnNumber(), columnWidth.getWidth() * 256);
            }
        }
    }

    private static void autoResize(int maxCol, XSSFSheet sheet, ExcelConfiguration excelConfiguration) {
        for (Integer colIdx : excelConfiguration.getAutoResizeColumns()) {
            if (colIdx <= maxCol) {
                log.debug("Setting auto-resize on column {}", colIdx);
                sheet.autoSizeColumn(colIdx);
            }
        }
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
