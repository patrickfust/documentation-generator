package dk.fust.docgen.excel.format.table;

import dk.fust.docgen.excel.format.table.model.ExcelColor;
import dk.fust.docgen.excel.format.table.model.ExcelConfiguration;
import dk.fust.docgen.excel.format.table.model.ExcelStyle;
import dk.fust.docgen.excel.format.table.model.ExcelStyles;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.format.table.TableFormatter;
import dk.fust.docgen.util.Assert;
import lombok.Data;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Formats a FormatTable into an Excel workbook.
 * The binary content is encoded as base64, and may be written to a file using {@link dk.fust.docgen.destination.Base64FileDestination}
 */
@Data
public class ExcelBase64TableFormatter implements TableFormatter {

    private ExcelStyle headerExcelStyle = new ExcelStyle("Verdana", (short) 11, true, new ExcelColor(IndexedColors.WHITE), new ExcelColor(IndexedColors.DARK_BLUE));
    private ExcelStyle secondaryHeaderExcelStyle = new ExcelStyle("Verdana", (short) 9, true, new ExcelColor(IndexedColors.WHITE), new ExcelColor(IndexedColors.LIGHT_BLUE));
    private ExcelStyle evenRowExcelStyle = new ExcelStyle("Verdana", (short) 9, false, new ExcelColor(IndexedColors.BLACK), new ExcelColor("220, 220, 220"));
    private ExcelStyle oddRowExcelStyle = new ExcelStyle("Verdana", (short) 9, false, new ExcelColor(IndexedColors.BLACK), new ExcelColor("240, 240, 240"));

    private int defaultColumnWidth = 50;

    private List<Integer> autoResizeColumns = new ArrayList<>();

    @Override
    public String formatTable(FormatTable formatTable) {
        ExcelConfiguration excelConfiguration = makeExcelConfiguration();
        try (XSSFWorkbook workbook = FormatTableToExcel.toExcel(formatTable, excelConfiguration)) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            workbook.write(baos);
            baos.close();
            return Base64.getEncoder().encodeToString(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ExcelConfiguration makeExcelConfiguration() {
        ExcelConfiguration excelConfiguration = new ExcelConfiguration();
        excelConfiguration.setExcelStyles(createExcelStyles());
        excelConfiguration.setDefaultColumnWidth(defaultColumnWidth);
        excelConfiguration.setAutoResizeColumns(autoResizeColumns);
        return excelConfiguration;
    }

    private ExcelStyles createExcelStyles() {
        validateStyle(headerExcelStyle);
        validateStyle(secondaryHeaderExcelStyle);
        validateStyle(evenRowExcelStyle);
        validateStyle(oddRowExcelStyle);
        return new ExcelStyles(headerExcelStyle, secondaryHeaderExcelStyle, oddRowExcelStyle, evenRowExcelStyle);
    }

    private void validateStyle(ExcelStyle excelStyle) {
        Assert.isNotNull(excelStyle, "excelStyle must not be null");
        Assert.isNotNull(excelStyle.getBackgroundColor(), "excelStyle.getBackgroundColor() must not be null");
        Assert.isNotNull(excelStyle.getFontColor(), "excelStyle.getFontColor() must not be null");
        Assert.isTrue(excelStyle.getFontHeightInPoints() > 0, "excelStyle.getFontHeightInPoints() must be greater than 0");
    }

}
