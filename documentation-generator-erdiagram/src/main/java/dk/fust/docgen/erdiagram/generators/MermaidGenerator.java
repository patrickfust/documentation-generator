package dk.fust.docgen.erdiagram.generators;

import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.Field;
import dk.fust.docgen.model.Generation;
import dk.fust.docgen.model.Table;

import java.util.List;
import java.util.stream.Stream;

/**
 * Generates ER-diagram in mermaid format
 */
public class MermaidGenerator implements ERGenerator {

    private static final int INITIAL_CAPACITY = 512;

    @Override
    public String getMarkdownType() {
        return "mermaid";
    }

    @Override
    public String generateUML(String filter, Documentation documentation, GeneratorConfiguration generatorConfiguration) {
        StringBuilder uml = new StringBuilder(INITIAL_CAPACITY);
        if (documentation.getDocumentationTitle() != null && !documentation.getDocumentationTitle().isEmpty()) {
            uml.append("""
---
title: %s
---
""".formatted(documentation.getDocumentationTitle()));
        }
        uml.append("erDiagram\n");
        String tables = generateTables(filter, documentation);
        uml.append(tables);
        return uml.toString();
    }

    private String generateTables(String filter, Documentation documentation) {
        StringBuilder uml = new StringBuilder(INITIAL_CAPACITY);

        List<Table> tables = documentation.filterTables(filter);
        tables.forEach(table -> {
            uml.append(generateTableForeignKeys(table));

            uml.append("%s {\n".formatted(table.getName()));
            Generation generationForTable = documentation.getGenerationForTable(table);
            if (generationForTable.isGenerateId()) {
                uml.append("    %s %s_id\n".formatted(generationForTable.getGenerateIdDataType().toLowerCase(), table.getName()));
            }
            if (table.getFields() != null) {
                table.getFields().forEach(field -> {
                    uml.append("    %s %s".formatted(field.getDataType(), field.getName()));
                    if (field.isPrimaryKey()) {
                        uml.append(" PK");
                    }
                    if (field.getForeignKey() != null) {
                        if (field.isPrimaryKey()) {
                            uml.append(", FK");
                        } else {
                            uml.append(" FK");
                        }
                    }
                    uml.append("\n");
                });
            }
            uml.append("}\n");
        });

        return uml.toString();
    }

    private String generateTableForeignKeys(Table table) {
        StringBuilder uml = new StringBuilder();
        if (table.getFields() != null) {
            Stream<Field> foreignKeys = table.getFields().stream().filter(t -> t.getForeignKey() != null);
            foreignKeys.forEach(foreignKey -> {
                String formattedForeignKey = "    %s ||--o{ %s : \"\"\n".formatted(
                        foreignKey.getForeignKey().getTableName(),
                        table.getName());
                uml.append(formattedForeignKey);
            });
        }
        return uml.toString();
    }
}
