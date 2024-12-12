package dk.fust.docgen.datalineage;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.format.table.Cell;
import dk.fust.docgen.format.table.Col;
import dk.fust.docgen.format.table.ColGroup;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.format.table.Row;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.Field;
import dk.fust.docgen.model.Generation;
import dk.fust.docgen.model.Table;
import dk.fust.docgen.service.DocumentationService;
import dk.fust.docgen.util.Assert;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Generator for Data Lineage
 */
@Slf4j
public class DataLineageGenerator implements Generator {

    private final Map<String, Documentation> keyToExternalDocumentation = new HashMap<>();
    private final DocumentationService documentationService = new DocumentationService();

    @Override
    public void generate(Documentation documentation, GeneratorConfiguration generatorConfiguration) throws IOException {
        log.info("Generating Data lineage...");
        Assert.isTrue(generatorConfiguration instanceof DataLineageConfiguration, "configuration must be of type DataLineageConfiguration");
        DataLineageConfiguration conf = (DataLineageConfiguration) generatorConfiguration;
        FormatTable formatTable = new FormatTable();
        formatTable.setTableClass(conf.getKey());
        formatTable.setColGroup(getColGroup());
        List<Row> rows = new ArrayList<>();
        formatTable.setRows(rows);

        rows.add(getHeaderRow());
        rows.add(getSubHeaderRow());
        rows.addAll(getRows(documentation.filterTables(conf.getFilterTags()), documentation, conf));

        String document = conf.getTableFormatter().formatTable(formatTable);
        generatorConfiguration.getDestination().sendDocumentToDestination(document, conf.getKey());
    }

    private List<Row> getRows(List<Table> tables, Documentation documentation, DataLineageConfiguration configuration) throws IOException {
        List<Row> rows = new ArrayList<>();
        if (tables != null) {
            for (Table table : tables) {
                rows.addAll(getRows(table, documentation, configuration));
            }
        }
        return rows;
    }

    private List<Row> getRows(Table table, Documentation documentation, DataLineageConfiguration configuration) throws IOException {
        List<Row> rows = new ArrayList<>();
        if (table.getFields() != null) {
            for (Field field : table.getFields()) {
                Row row = new Row();
                List<Cell> cells = new ArrayList<>();
                row.setCells(cells);

                cells.add(new Cell(table.getName()));
                cells.add(new Cell(field.getName()));
                cells.add(new Cell(field.getDataType().toLowerCase()));

                if (field.getSource() != null) {
                    addCellsForSource(cells, field.getSource(), documentation, configuration);
                } else {
                    cells.add(new Cell(""));
                    cells.add(new Cell(""));
                    cells.add(new Cell(""));
                    cells.add(new Cell(""));
                }
                if (field.getTransformation() != null) {
                    cells.add(new Cell(field.getTransformation()));
                } else {
                    cells.add(new Cell(""));
                }
                rows.add(row);
            }
        }
        return rows;
    }

    private void addCellsForSource(List<Cell> cells, String source, Documentation documentation, DataLineageConfiguration configuration) throws IOException {
        Documentation sourceDocumentation = getSourceFieldDocumentation(source, configuration, documentation);
        String[] split = source.split("[.]");
        String tableName = split[split.length - 2];
        String fieldName = split[split.length - 1];
        String database = sourceDocumentation.getDatabaseName() != null ? sourceDocumentation.getDatabaseName() : "";
        Table sourceTable = sourceDocumentation.getTable(tableName);
        Generation sourceDocumentationGenerationForTable = sourceDocumentation.getGenerationForTable(sourceTable);
        Field sourceField = sourceTable.getField(fieldName, sourceDocumentationGenerationForTable.getGenerateIdDataType(), documentation);
        Assert.isNotNull(sourceField, "sourceField must not be null for source " + source);
        cells.add(new Cell(database));
        cells.add(new Cell(tableName));
        cells.add(new Cell(fieldName));
        cells.add(new Cell(sourceField.getDataType().toLowerCase()));
    }

    private Documentation getSourceFieldDocumentation(String source, DataLineageConfiguration conf, Documentation documentation) throws IOException {
        Documentation documentationToReturn;
        String[] split = source.split("[.]");
        if (split.length == 3) {
            // We're pointing to an external Documentation model file
            String key = split[0];
            if (keyToExternalDocumentation.containsKey(key)) {
                documentationToReturn = keyToExternalDocumentation.get(key);
            } else {
                File fileForExternalDocumentationFile = conf.getSourceDocumentationFiles().get(key);
                Assert.isNotNull(fileForExternalDocumentationFile, "source documentation file with key %s can't be found".formatted(key));
                documentationToReturn = documentationService.loadDocumentation(fileForExternalDocumentationFile);
                Assert.isNotNull(documentationToReturn, "source documentation file with key %s can't be loaded as Documentation".formatted(key));
                keyToExternalDocumentation.put(key, documentationToReturn);
            }
        } else if (split.length == 2) {
            documentationToReturn = documentation;
        } else {
            throw new IllegalArgumentException("Length of source may be 2 or 3, not " + split.length + " for source " + source);
        }
        Assert.isNotNull(documentationToReturn, "Can't find source with name " + source);
        return documentationToReturn;
    }

    private static ColGroup getColGroup() {
        ColGroup colGroup = new ColGroup();
        colGroup.setCols(List.of(new Col(8)));
        return colGroup;
    }

    private static Row getSubHeaderRow() {
        Row row = new Row();
        row.setCells(List.of(
                new Cell("Table Name"),
                new Cell("Column Name"),
                new Cell("Data Type"),
                new Cell("Database Name"),
                new Cell("Table Name"),
                new Cell("Column Name"),
                new Cell("Data Type")
        ));
        return row;
    }

    private static Row getHeaderRow() {
        Row row = new Row();
        row.setCells(List.of(
                new Cell(3, "Target", true),
                new Cell(4, "Source", true),
                new Cell(1, "Transformation", true)
        ));
        return row;
    }
}
