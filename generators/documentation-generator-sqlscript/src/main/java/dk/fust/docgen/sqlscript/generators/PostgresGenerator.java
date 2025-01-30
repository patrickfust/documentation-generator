package dk.fust.docgen.sqlscript.generators;

import dk.fust.docgen.model.DataType;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.Field;
import dk.fust.docgen.model.Generation;
import dk.fust.docgen.model.Index;
import dk.fust.docgen.model.Table;
import dk.fust.docgen.model.View;
import dk.fust.docgen.sqlscript.SqlScriptConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates SQL for Postgres databases
 */
@Slf4j
public class PostgresGenerator implements SqlGenerator {

    @Override
    public void generate(Documentation documentation, SqlScriptConfiguration sqlScriptConfiguration) throws IOException {
        log.debug("Generating Postgres SQL-scripts...");
        String schemaName = documentation.getSchemaName();
        for (Table table : documentation.filterTables(sqlScriptConfiguration.getFilterTags())) {
            Generation generationForTable = documentation.getGenerationForTable(table);
            if (table.getCreateTableScript() != null) {
                StringBuilder sqlScript = new StringBuilder(256);

                sqlScript.append(generateTable(schemaName, table, generationForTable));
                sqlScript.append(describeTable(schemaName, table, generationForTable));
                sqlScript.append(generateViews(schemaName, table));
                sqlScript.append(generateIndexes(schemaName, table));
                sqlScript.append(describeIndexes(schemaName, table));
                sqlScript.append(generateTriggers(schemaName, table, generationForTable));

                sqlScriptConfiguration.getDestination()
                        .sendDocumentToDestination(sqlScript.toString(), table.getCreateTableScript());
            }
        }
    }

    @SuppressWarnings("ReassignedVariable")
    private String generateTable(String schemaName, Table table, Generation generationForTable) {
        boolean hasColumn = false;
        StringBuilder sql = new StringBuilder(128);
        sql.append("create table ");
        appendName(schemaName, table.getName(), sql).append(" (\n");

        if (generationForTable.hasGenerateId() && generationForTable.getGenerateId()) {
            sql.append("    %s_id %s primary key generated always as identity".formatted(table.getName(), generationForTable.getGenerateIdDataType().toLowerCase()));
            hasColumn = true;
        }
        if (generationForTable.getAddCreatedAt() != null && generationForTable.getAddCreatedAt()) {
            if (hasColumn) {
                sql.append(",\n");
            } else {
                hasColumn = true;
            }
            sql.append("    ").append(generationForTable.getColumnNameCreatedAt()).append(" timestamp with time zone not null default now()");
        }
        if (generationForTable.getAddUpdatedAt() != null && generationForTable.getAddUpdatedAt()) {
            if (hasColumn) {
                sql.append(",\n");
            } else {
                hasColumn = true;
            }
            sql.append("    ").append(generationForTable.getColumnNameUpdatedAt()).append(" timestamp with time zone not null default now()");
        }
        generateTableFields(table, hasColumn, sql);
        sql.append("\n);\n");
        return sql.toString();
    }

    private void generateTableFields(Table table, boolean hasColumn, StringBuilder sql) {
        List<Field> primaryKeys = new ArrayList<>();
        for (Field field : table.getFields()) {
            if (hasColumn) {
                sql.append(",\n");
            } else {
                hasColumn = true;
            }
            sql.append("    %s %s".formatted(field.getName(), field.getDataType().toLowerCase()));
            if (field.isUnique()) {
                sql.append(" unique");
            }
            if (!field.isNullable()) {
                sql.append(" not null");
            }
            if (field.isPrimaryKey()) {
                primaryKeys.add(field);
            }
            if (field.getForeignKey() != null && field.getForeignKey().isEnforceReference()) {
                sql.append(" references %s(%s)".formatted(
                        field.getForeignKey().getTableName(), field.getForeignKey().getColumnName()));
            }
            if (field.getDefaultValue() != null && !field.getDefaultValue().isEmpty()) {
                sql.append(" default '%s'".formatted(field.getDefaultValue()));
            }
            sql.append(generateCheckConstraint(field));
        }
        if (!primaryKeys.isEmpty()) {
            sql.append(",\n    primary key(");
            String keys = primaryKeys.stream().map(Field::getName).collect(Collectors.joining(", "));
            sql.append(keys).append(")");
        }
    }

    private String generateCheckConstraint(Field field) {
        if (field.getCheck() == null || field.getCheck().isEmpty()) {
            return "";
        }
        StringBuilder sql = new StringBuilder(" check (%s in (".formatted(field.getName()));
        if (field.getDataType().equals(DataType.TEXT)) {
            sql.append(field.getCheck().stream().collect(Collectors.joining("', '", "'", "'")));
        } else {
            sql.append(String.join(", ", field.getCheck()));
        }
        sql.append("))");
        return sql.toString();
    }

    private String describeTable(String schemaName, Table table, Generation generationForTable) {
        StringBuilder sql = new StringBuilder(128).append("\n");
        if (table.getComment() != null) {
            sql.append("comment on table ");
            appendName(schemaName, table.getName(), sql);
            sql.append(" is '%s';\n".formatted(table.getComment().replaceAll("'", "''")));
        }
        if (generationForTable.getAddCreatedAt() != null && generationForTable.getAddCreatedAt()) {
            sql.append("comment on column ");
            appendName(schemaName, table.getName(), sql).append(".%s".formatted(generationForTable.getColumnNameCreatedAt())).append(" is 'timestamp when row is inserted';\n");
        }
        if (generationForTable.getAddUpdatedAt() != null && generationForTable.getAddUpdatedAt()) {
            sql.append("comment on column ");
            appendName(schemaName, table.getName(), sql).append(".%s".formatted(generationForTable.getColumnNameUpdatedAt())).append(" is 'timestamp when row is last updated';\n");
        }
        table.getFields().forEach(field -> {
            if (field.getComment() != null) {
                sql.append("comment on column ");
                appendName(schemaName, table.getName(), sql).append(".%s is '".formatted(field.getName()));
                sql.append(field.getComment().replaceAll("'", "''"));
                sql.append("';\n");
            }
        });
        if (sql.length() > 3) {
            return sql.toString();
        }
        // Don't have anything
        return "";
    }

    private String describeIndexes(String schemaName, Table table) {
        StringBuilder sql = new StringBuilder(128);
        if (table != null && table.getIndexes() != null) {
            table.getIndexes().forEach(index -> {
                if (index.getComment() != null) {
                    if (sql.isEmpty()) {
                        sql.append("\n");
                    }
                    sql.append("comment on index ");
                    appendName(schemaName, index.getName(), sql);
                    sql.append(" is '%s';\n".formatted(index.getComment().replaceAll("'", "''")));
                }
            });
        }
        return sql.toString();
    }

    private String generateViews(String schemaName, Table table) {
        StringBuilder sql = new StringBuilder(128);
        if (table.getViews() != null && !table.getViews().isEmpty()) {
            for (View views : table.getViews()) {
                sql.append("\ndrop view if exists ");
                appendName(schemaName, views.getName(), sql).append(";\n");
                sql.append("create view ");
                appendName(schemaName, views.getName(), sql).append(" as \n    ");
                sql.append(views.getSql());
                sql.append(";\n");
            }
        }
        return sql.toString();
    }

    private String generateIndexes(String schemaName, Table table) {
        StringBuilder sql = new StringBuilder(128);
        if (table.getIndexes() != null && !table.getIndexes().isEmpty()) {
            for (Index index : table.getIndexes()) {
                sql.append("\ncreate");
                if (index.isUnique()) {
                    sql.append(" unique");
                }
                sql.append(" index %s on ".formatted(index.getName()));
                appendName(schemaName, table.getName(), sql);
                sql.append("(");
                boolean first = true;
                for (String field : index.getFields()) {
                    if (!first) {
                        sql.append(", ");
                    } else {
                        first = false;
                    }
                    sql.append(field);
                }
                sql.append(");\n");
            }
        }
        return sql.toString();
    }

    private String generateTriggers(String schemaName, Table table, Generation generationForTable) {
        StringBuilder sql = new StringBuilder(128);
        if (generationForTable.getTriggerForUpdates() != null && !generationForTable.getTriggerForUpdates().isEmpty()) {
            sql.append(("\n" +
                    "create trigger %s_update_time\n    before update\n    on ").formatted(table.getName()));
            appendName(schemaName, table.getName(), sql).append("\n");
            sql.append("    for each row execute function ");
            appendName(schemaName, generationForTable.getTriggerForUpdates(), sql).append("();\n");
        }
        return sql.toString();
    }

    private StringBuilder appendName(String schemaName, String fieldName, StringBuilder stringBuilder) {
        if (schemaName != null) {
            stringBuilder.append(schemaName).append(".");
        }
        stringBuilder.append(fieldName);
        return stringBuilder;
    }

}
