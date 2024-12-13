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
        if (dataDictionaryConfiguration.isExportSchema()) {
            Assert.isNotNull(dataDictionaryConfiguration.getSchemaName(), "Schema name must be set when exporting schema");
            addCell(cells, dataDictionaryConfiguration.getSchemaName(), dataDictionaryConfiguration.getAlignmentSchema());
        }
        if (dataDictionaryConfiguration.isExportFilename()) {
            addCell(cells, dataDictionaryFile.getFileName(), dataDictionaryConfiguration.getAlignmentFilename());
        }
        if (dataDictionaryConfiguration.isExportTableName()) {
            addCell(cells, dataDictionaryFile.getTableName(), dataDictionaryConfiguration.getAlignmentTableName());
        }
        if (dataDictionaryConfiguration.isExportColumn()) {
            addCell(cells, column.getColumnName(), dataDictionaryConfiguration.getAlignmentColumn());
        }
        if (dataDictionaryConfiguration.isExportPosition()) {
            Cell cell = new Cell(position);
            cell.setAlignment(dataDictionaryConfiguration.getAlignmentPosition());
            cells.add(cell);
        }
        if (dataDictionaryConfiguration.isExportDataType()) {
            addCell(cells, column.getDataType(), dataDictionaryConfiguration.getAlignmentDataType());
        }
        if (dataDictionaryConfiguration.isExportMandatory()) {
            addCell(cells, column.getMandatory() ? "Yes" : "No", dataDictionaryConfiguration.getAlignmentMandatory());
        }
        if (dataDictionaryConfiguration.isExportKeys()) {
            addCell(cells, column.getKeys(), dataDictionaryConfiguration.getAlignmentKeys());
        }
        if (dataDictionaryConfiguration.isExportDescription()) {
            addCell(cells, column.getColumnDescription(), dataDictionaryConfiguration.getAlignmentDescription());
        }
        if (dataDictionaryConfiguration.isExportExample()) {
            addCell(cells, column.getExample(), dataDictionaryConfiguration.getAlignmentExample());
        }
        return row;
    }

    private static void addCell(List<Cell> cells, String content, Alignment alignment) {
        Cell cell = new Cell(content != null ? content : "");
        cell.setAlignment(alignment);
        cells.add(cell);
    }

    private static Row createDescriptionForFileRow(DataDictionaryFile dataDictionaryFile, DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row row = new Row();
        List<Cell> cells = row.getCells();
        if (dataDictionaryConfiguration.isExportSchema()) {
            cells.add(new Cell((String) null, true));
        }
        if (dataDictionaryConfiguration.isExportFilename()) {
            cells.add(new Cell(dataDictionaryFile.getFileName(), true));
        }
        if (dataDictionaryConfiguration.isExportTableName()) {
            cells.add(new Cell(dataDictionaryFile.getTableName(), true));
        }
        int emptyCells = 0;
        emptyCells += dataDictionaryConfiguration.isExportColumn() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportMandatory() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportKeys() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportPosition() ? 1 : 0;
        emptyCells += dataDictionaryConfiguration.isExportDataType() ? 1 : 0;
        for (int i = 0; i < emptyCells; i++) {
            cells.add(new Cell((String) null, true));
        }
        if (dataDictionaryConfiguration.isExportDescription()) {
            cells.add(new Cell(dataDictionaryFile.getFileDescription(), true));
        }
        if (dataDictionaryConfiguration.isExportExample()) {
            cells.add(new Cell((String) null, true));
        }
        return row;
    }

    private static Row createHeaderRow(DataDictionaryConfiguration dataDictionaryConfiguration) {
        Row headerRow = new Row();
        List<Cell> headerCells = headerRow.getCells();
        if (dataDictionaryConfiguration.isExportSchema()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderSchema(), true));
        }
        if (dataDictionaryConfiguration.isExportFilename()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderFilename(), true, dataDictionaryConfiguration.getAlignmentFilename()));
        }
        if (dataDictionaryConfiguration.isExportTableName()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderTableName(), true, dataDictionaryConfiguration.getAlignmentFilename()));
        }
        if (dataDictionaryConfiguration.isExportColumn()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderColumn(), true, dataDictionaryConfiguration.getAlignmentColumn()));
        }
        if (dataDictionaryConfiguration.isExportPosition()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderPosition(), true, dataDictionaryConfiguration.getAlignmentPosition()));
        }
        if (dataDictionaryConfiguration.isExportDataType()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderDataType(), true, dataDictionaryConfiguration.getAlignmentDataType()));
        }
        if (dataDictionaryConfiguration.isExportMandatory()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderMandatory(), true, dataDictionaryConfiguration.getAlignmentMandatory()));
        }
        if (dataDictionaryConfiguration.isExportKeys()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderKeys(), true, dataDictionaryConfiguration.getAlignmentKeys()));
        }
        if (dataDictionaryConfiguration.isExportDescription()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderDescription(), true, dataDictionaryConfiguration.getAlignmentDescription()));
        }
        if (dataDictionaryConfiguration.isExportExample()) {
            headerCells.add(new Cell(1, dataDictionaryConfiguration.getHeaderExample(), true, dataDictionaryConfiguration.getAlignmentExample()));
        }
        return headerRow;
    }

    private void sendTableToDestination(FormatTable formatTable, DataDictionaryConfiguration dataDictionaryConfiguration) throws IOException {
        String table = dataDictionaryConfiguration.getTableFormatter().formatTable(formatTable);
        dataDictionaryConfiguration.getDestination().sendDocumentToDestination(table, dataDictionaryConfiguration.getKey());
    }

}
