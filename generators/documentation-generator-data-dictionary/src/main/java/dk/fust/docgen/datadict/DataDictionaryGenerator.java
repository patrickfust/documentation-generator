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
        formatTable.getRows().add(createHeaderRow());
        for (DataDictionaryFile dataDictionaryFile : documentation.getDataDictionary().getDataDictionaryFiles()) {
            if (dataDictionaryConfiguration.isAddDescriptionForFile()) {
                Row row = new Row();
                List<Cell> cells = row.getCells();
                cells.add(new Cell(dataDictionaryFile.getFileName()));
                cells.add(new Cell(null));
                cells.add(new Cell(null));
                cells.add(new Cell(null));
                cells.add(null);
                cells.add(null);
                cells.add(new Cell(dataDictionaryFile.getFileDescription()));
                formatTable.getRows().add(row);
            }
            int position = 1;
            for (Column column : dataDictionaryFile.getColumns()) {
                Row row = new Row();
                List<Cell> cells = row.getCells();
                cells.add(new Cell(dataDictionaryFile.getFileName()));
                cells.add(new Cell(column.getColumnName()));
                cells.add(new Cell(Integer.toString(position++)));
                cells.add(new Cell(column.getDataType()));
                cells.add(new Cell(column.getMandatory() ? "Yes" : "No"));
                cells.add(new Cell(column.getKeys()));
                cells.add(new Cell(column.getColumnDescription()));
                formatTable.getRows().add(row);
            }
        }
        return formatTable;
    }

    private static Row createHeaderRow() {
        Row headerRow = new Row();
        List<Cell> headerCells = headerRow.getCells();
        headerCells.add(new Cell(1, 1, "Filename", true));
        headerCells.add(new Cell(1, 1, "Column", true));
        headerCells.add(new Cell(1, 1, "Position", true));
        headerCells.add(new Cell(1, 1, "Type", true));
        headerCells.add(new Cell(1, 1, "Mandatory", true));
        headerCells.add(new Cell(1, 1, "Keys", true));
        headerCells.add(new Cell(1, 1, "Description", true));
        return headerRow;
    }

    private void sendTableToDestination(FormatTable formatTable, DataDictionaryConfiguration dataDictionaryConfiguration) throws IOException {
        String table = dataDictionaryConfiguration.getTableFormatter().formatTable(formatTable);
        dataDictionaryConfiguration.getDestination().sendDocumentToDestination(table, dataDictionaryConfiguration.getKey());
    }

}
