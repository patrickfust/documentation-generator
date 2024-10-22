package dk.fust.docgen.erdiagram.generators;

import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.Field;
import dk.fust.docgen.model.Generation;
import dk.fust.docgen.model.Table;

import java.util.List;

/**
 * Generates ER-diagram in plantuml format
 */
public class PlantUMLGenerator implements ERGenerator {
    private static final String PROCEDURES = """
!procedure $schema($name)
    package "$name" <<Rectangle>>
!endprocedure
!procedure $table($name)
    entity "<b>$name</b>" as $name << (T, Orange) table >>
!endprocedure
!procedure $view($name)
    entity "<b>$name</b>" as $name << (V, Aquamarine) view >>
!endprocedure
!procedure $pk($name)
    <color:#GoldenRod><&key></color> <b>$name</b>
!endprocedure
!procedure $fk($name)
    <color:#Silver><&key></color> $name
!endprocedure
!procedure $column($name)
   {field} <color:#White><&media-record></color> $name
!endprocedure""";

    @Override
    public String getMarkdownType() {
        return "plantuml";
    }

    @Override
    public String generateUML(String filter, Documentation documentation, GeneratorConfiguration generatorConfiguration) {
        StringBuilder stringBuilder = new StringBuilder(1024);
        List<Table> tables = documentation.filterTables(filter);
        stringBuilder.append("""
@startuml

!theme plain
hide empty methods

%s
""".formatted(PROCEDURES));
        if (documentation.getDocumentationTitle() != null) {
            stringBuilder.append("title \"%s\"\n".formatted(documentation.getDocumentationTitle()));
        }
        if (documentation.getSchemaName() != null) {
            stringBuilder.append("$schema(\"%s\") {\n".formatted(documentation.getSchemaName()));
        }
        stringBuilder.append(generateTables(tables, documentation));
        if (documentation.getSchemaName() != null) {
            stringBuilder.append("}\n");
        }
        stringBuilder.append(generateForeignKeys(tables, documentation));
        stringBuilder.append("@enduml");
        return stringBuilder.toString();
    }

    private String generateTables(List<Table> tables, Documentation documentation) {
        StringBuilder uml = new StringBuilder(128);
        for (Table table : tables) {
            uml.append(generateTable(table, documentation));
        }
        return uml.toString();
    }

    private static String generateTable(Table table, Documentation documentation) {
        StringBuilder uml = new StringBuilder(128);
        uml.append("  $table(\"%s\") {\n".formatted(table.getName()));
        Generation generationForTable = documentation.getGenerationForTable(table);
        if (generationForTable.isGenerateId()) {
            uml.append("    $pk(\"%s_id\"): %s NOT NULL\n".formatted(table.getName(), generationForTable.getGenerateIdDataType().toLowerCase()));
        }
        table.getFields().forEach(field -> {
            if (field.getForeignKey() != null) {
                uml.append(key("fk", field));
            } else if (field.isPrimaryKey()) {
                uml.append(key("pk", field));
            } else {
                uml.append(key("column", field));
            }
        });
        uml.append("  }\n");
        return uml.toString();
    }

    private static String key(String key, Field field) {
        return "    $%s(\"%s\"): %s\n".formatted(key, field.getName(), field.getDataType().toLowerCase());
    }

    private static String generateForeignKeys(List<Table> tables, Documentation documentation) {
        StringBuilder uml = new StringBuilder(128);
        tables.forEach(table -> {
            table.getFields().forEach(field -> {
                if (field.getForeignKey() != null) {
                    if (documentation.getSchemaName() != null) {
                        uml.append("%s.%s::%s ||--o{ %s.%s::%s\n".formatted(
                                documentation.getSchemaName(), field.getForeignKey().getTableName(), field.getForeignKey().getColumnName(),
                                documentation.getSchemaName(), table.getName(), field.getName()));
                    } else {
                        uml.append("%s::%s ||--o{ %s::%s\n".formatted(
                                field.getForeignKey().getTableName(), field.getForeignKey().getColumnName(),
                                table.getName(), field.getName()));
                    }
                }
            });
        });
        return uml.toString();
    }
}
