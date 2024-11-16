package dk.fust.docgen.datadict;

import dk.fust.docgen.format.table.Cell;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.format.table.Row;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.datadict.DataDictionaryFile;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Generator that can generate meta information for data dictionaries
 */
@Slf4j
public class DataDictionaryMetaInfoGenerator extends AbstractDataDictionaryGenerator {

    @Override
    protected FormatTable generateTable(Documentation documentation, AbstractDataDictionaryConfiguration configuration) {
        log.info("Generating Data Dictionary Meta Info...");
        Assert.isTrue(configuration instanceof DataDictionaryMetaInfoConfiguration, "must be a DataDictionaryMetaInfoConfiguration");
        FormatTable formatTable = new FormatTable();

        formatTable.getRows().add(createHeaderRow());
        List<DataDictionaryFile> dataDictionaryFiles = documentation.filterDataDictionaryFiles(configuration.getFilterTags());
        for (DataDictionaryFile dataDictionaryFile : dataDictionaryFiles) {
            Row row = new Row();
            List<Cell> cells = row.getCells();
            cells.add(new Cell(dataDictionaryFile.getFileName()));
            cells.add(new Cell(dataDictionaryFile.getVersion()));
            if (dataDictionaryFile.getFileDescription() != null) {
                cells.add(new Cell(dataDictionaryFile.getFileDescription()));
            }
            formatTable.getRows().add(row);
        }
        return formatTable;
    }

    private static Row createHeaderRow() {
        Row headerRow = new Row();
        List<Cell> headerCells = headerRow.getCells();
        headerCells.add(new Cell(1, 1, "Filename", true));
        headerCells.add(new Cell(1, 1, "Version", true));
        headerCells.add(new Cell(1, 1, "Description", true));
        return headerRow;
    }

}
