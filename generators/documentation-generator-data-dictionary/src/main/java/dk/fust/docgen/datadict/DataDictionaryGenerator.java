package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.format.table.Alignment;
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
            long position = 1;
            for (Column column : dataDictionaryFile.getColumns()) {
                Row row = createRowForColumn(column, position, dataDictionaryFile, dataDictionaryConfiguration);
                position++;
                formatTable.getRows().add(row);
            }
        }
        return formatTable;
    }

    private static Row createRowForColumn(Column column, long position, DataDictionaryFile dataDictionaryFile, DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row row = new Row();
        List<Cell> cells = row.getCells();
        if (dataDictionaryConfiguration.getColumnSchema().getExport()) {
            Assert.isNotNull(dataDictionaryConfiguration.getSchemaName(), "Schema name must be set when exporting schema");
            addCell(cells, dataDictionaryConfiguration.getSchemaName(), dataDictionaryConfiguration.getColumnSchema().getAlignment());
        }
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnFilename(), dataDictionaryFile.getFileName());
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnTable(), dataDictionaryFile.getTableName());
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnColumn(), column.getColumnName());
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnPosition(), position);
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnType(), column.getDataType());
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnMandatory(), column.getMandatory() ? "Yes" : "No");
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnKeys(), column.getKeys());
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnDescription(), column.getColumnDescription());
        addCellIfConfigured(cells, dataDictionaryConfiguration.getColumnExample(), column.getExample());
        return row;
    }

    private static void addCellIfConfigured(List<Cell> cells, DataDictionaryConfigurationColumn dataDictionaryConfigurationColumn, String content) {
        if (dataDictionaryConfigurationColumn.getExport()) {
            addCell(cells, content, dataDictionaryConfigurationColumn.getAlignment());
        }
    }

    private static void addCellIfConfigured(List<Cell> cells, DataDictionaryConfigurationColumn dataDictionaryConfigurationColumn, long contentLong) {
        if (dataDictionaryConfigurationColumn.getExport()) {
            addCell(cells, contentLong, dataDictionaryConfigurationColumn.getAlignment());
        }
    }

    private static void addCell(List<Cell> cells, String content, Alignment alignment) {
        Cell cell = new Cell(content != null ? content : "");
        cell.setAlignment(alignment);
        cells.add(cell);
    }

    private static void addCell(List<Cell> cells, long contentLong, Alignment alignment) {
        Cell cell = new Cell(contentLong);
        cell.setAlignment(alignment);
        cells.add(cell);
    }

    private static Row createDescriptionForFileRow(DataDictionaryFile dataDictionaryFile, DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row row = new Row();
        List<Cell> cells = row.getCells();
        if (dataDictionaryConfiguration.getColumnSchema().getExport()) {
            cells.add(new Cell((String) null, true));
        }
        if (dataDictionaryConfiguration.getColumnFilename().getExport()) {
            cells.add(new Cell(dataDictionaryFile.getFileName(), true));
        }
        if (dataDictionaryConfiguration.getColumnTable().getExport()) {
            cells.add(new Cell(dataDictionaryFile.getTableName(), true));
        }
        int emptyCells = 0;
        emptyCells += dataDictionaryConfiguration.getColumnColumn().getExport() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.getColumnMandatory().getExport() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.getColumnKeys().getExport() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.getColumnPosition().getExport() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.getColumnType().getExport() ? 1 : 0;
        for (int i = 0; i < emptyCells; i++) {
            cells.add(new Cell((String) null, true));
        }
        if (dataDictionaryConfiguration.getColumnDescription().getExport()) {
            cells.add(new Cell(dataDictionaryFile.getFileDescription(), true));
        }
        if (dataDictionaryConfiguration.getColumnExample().getExport()) {
            cells.add(new Cell((String) null, true));
        }
        return row;
    }

    private static Row createHeaderRow(DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row headerRow = new Row();
        List<Cell> headerCells = headerRow.getCells();
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnSchema());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnFilename());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnTable());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnColumn());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnPosition());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnType());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnMandatory());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnKeys());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnDescription());
        addHeaderCellIfConfigured(headerCells, dataDictionaryConfiguration.getColumnExample());
        return headerRow;
    }

    private static void addHeaderCellIfConfigured(List<Cell> headerCells, DataDictionaryConfigurationColumn dataDictionaryConfigurationColumn) {
        if (dataDictionaryConfigurationColumn.getExport()) {
            headerCells.add(new Cell(1, dataDictionaryConfigurationColumn.getHeader(), true, dataDictionaryConfigurationColumn.getAlignment()));
        }
    }

    private void sendTableToDestination(FormatTable formatTable, DataDictionaryConfiguration dataDictionaryConfiguration) throws IOException {
        String table = dataDictionaryConfiguration.getTableFormatter().formatTable(formatTable);
        dataDictionaryConfiguration.getDestination().sendDocumentToDestination(table, dataDictionaryConfiguration.getKey());
    }

}
