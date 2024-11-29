package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.format.table.Cell;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.format.table.Row;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.datadict.Column;
import dk.fust.docgen.model.datadict.DataDictionaryFile;
import dk.fust.docgen.util.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;

/**
 * Generating data dictionaries
 */
@Slf4j
@Data
public class DataDictionaryGenerator implements Generator {

    @Override
    public void generate(Documentation documentation, GeneratorConfiguration generatorConfiguration) throws IOException {
        log.info("Generating Data Dictionary...");
        Assert.isTrue(generatorConfiguration instanceof DataDictionaryConfiguration, "Configuration must be DataDictionaryConfiguration");
        DataDictionaryConfiguration dataDictionaryConfiguration = (DataDictionaryConfiguration) generatorConfiguration;
        if (documentation.getDataDictionary() != null && documentation.getDataDictionary().getDataDictionaryFiles() != null) {
            sendTableToDestination(generateTable(documentation, dataDictionaryConfiguration), dataDictionaryConfiguration);
        }
    }

    private FormatTable generateTable(Documentation documentation, DataDictionaryConfiguration dataDictionaryConfiguration) {
        FormatTable formatTable = new FormatTable();
        formatTable.getRows().add(createHeaderRow(dataDictionaryConfiguration));
        List<DataDictionaryFile> dataDictionaryFiles = documentation.filterDataDictionaryFiles(dataDictionaryConfiguration.getFilterTags());
        for (DataDictionaryFile dataDictionaryFile : dataDictionaryFiles) {
            if (dataDictionaryConfiguration.isAddDescriptionForFile()) {
                Row row = createDescriptionForFileRow(dataDictionaryFile, dataDictionaryConfiguration);
                formatTable.getRows().add(row);
            }
            int position = 1;
            for (Column column : dataDictionaryFile.getColumns()) {
                Row row = createRowForColumn(column, position, dataDictionaryFile, dataDictionaryConfiguration);
                position++;
                formatTable.getRows().add(row);
            }
        }
        return formatTable;
    }

    private static Row createRowForColumn(Column column, int position, DataDictionaryFile dataDictionaryFile, DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row row = new Row();
        List<Cell> cells = row.getCells();
        if (dataDictionaryConfiguration.isExportFilename()) {
            cells.add(new Cell(dataDictionaryFile.getFileName()));
        }
        if (dataDictionaryConfiguration.isExportColumn()) {
            cells.add(new Cell(column.getColumnName()));
        }
        if (dataDictionaryConfiguration.isExportPosition()) {
            cells.add(new Cell(Integer.toString(position)));
        }
        if (dataDictionaryConfiguration.isExportDataType()) {
            cells.add(new Cell(column.getDataType()));
        }
        if (dataDictionaryConfiguration.isExportMandatory()) {
            cells.add(new Cell(column.getMandatory() ? "Yes" : "No"));
        }
        if (dataDictionaryConfiguration.isExportKeys()) {
            cells.add(new Cell(column.getKeys()));
        }
        if (dataDictionaryConfiguration.isExportDescription()) {
            cells.add(new Cell(column.getColumnDescription()));
        }
        return row;
    }

    private static Row createDescriptionForFileRow(DataDictionaryFile dataDictionaryFile, DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row row = new Row();
        List<Cell> cells = row.getCells();
        if (dataDictionaryConfiguration.isExportFilename()) {
            cells.add(new Cell(dataDictionaryFile.getFileName()));
        }
        int emptyCells = 0;
        emptyCells += dataDictionaryConfiguration.isExportColumn() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportMandatory() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportKeys() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportPosition() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportDataType() ? 1 : 0;
        for (int i = 0; i < emptyCells; i++) {
            cells.add(new Cell(null));
        }
        if (dataDictionaryConfiguration.isExportDescription()) {
            cells.add(new Cell(dataDictionaryFile.getFileDescription()));
        }
        return row;
    }

    private static Row createHeaderRow(DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row headerRow = new Row();
        List<Cell> headerCells = headerRow.getCells();
        if (dataDictionaryConfiguration.isExportFilename()) {
            headerCells.add(new Cell(1, 1, "Filename", true));
        }
        if (dataDictionaryConfiguration.isExportColumn()) {
            headerCells.add(new Cell(1, 1, "Column", true));
        }
        if (dataDictionaryConfiguration.isExportPosition()) {
            headerCells.add(new Cell(1, 1, "Position", true));
        }
        if (dataDictionaryConfiguration.isExportDataType()) {
            headerCells.add(new Cell(1, 1, "Type", true));
        }
        if (dataDictionaryConfiguration.isExportMandatory()) {
            headerCells.add(new Cell(1, 1, "Mandatory", true));
        }
        if (dataDictionaryConfiguration.isExportKeys()) {
            headerCells.add(new Cell(1, 1, "Keys", true));
        }
        if (dataDictionaryConfiguration.isExportDescription()) {
            headerCells.add(new Cell(1, 1, "Description", true));
        }
        return headerRow;
    }

    private void sendTableToDestination(FormatTable formatTable, DataDictionaryConfiguration dataDictionaryConfiguration) throws IOException {
        String table = dataDictionaryConfiguration.getTableFormatter().formatTable(formatTable);
        dataDictionaryConfiguration.getDestination().sendDocumentToDestination(table, dataDictionaryConfiguration.getKey());
    }

}
