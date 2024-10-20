package dk.fust.docgen;

import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.Field;
import dk.fust.docgen.model.Index;
import dk.fust.docgen.model.Table;
import dk.fust.docgen.model.View;
import dk.fust.docgen.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Validates that the model of the documentation is in order
 */
public class ModelValidator {

    private final Documentation documentation;

    /**
     * Constructor with the documentation to validate
     *
     * @param documentation documentation to validate
     */
    public ModelValidator(Documentation documentation) {
        this.documentation = documentation;
    }

    /**
     * Validate the documentation.
     */
    public void validate() {
        if (documentation == null) {
            throw new IllegalArgumentException("Documentation cannot be null");
        }
        validateTables();
    }

    private void validateTables() {
        if (documentation.getTables() != null) {
            documentation.getTables().forEach(this::validateTable);
        }
    }

    private void validateTable(Table table) {
        Assert.isNotNull(table.getName(), "Table without a name");
        Assert.isNotNull(table.getFields(), "Table " + table.getName() + " has no fields");
        table.getFields().forEach(f -> validateField(f, table));
        validateIndexes(table);
        validateViews(table);
    }

    private void validateField(Field field, Table table) {
        Assert.isNotNull(field.getName(), "Field without a name in table " + table.getName());
        Assert.isNotNull(field.getDataType(), "Field " + field.getName() + " has no data type in table " + table.getName());
        Assert.isTrue(!(field.isPrimaryKey() && documentation.getGenerationForTable(table).isGenerateId()), "Field " + field.getName() + " has primary key and is generating id");
        if (field.getForeignKey() != null) {
            String tableName = field.getForeignKey().getTableName();
            String columnName = field.getForeignKey().getColumnName();
            Assert.isNotNull(tableName, "Field " + field.getName() + " has foreign key without table name");
            Assert.isNotNull(columnName, "Field " + field.getName() + " has foreign key without column name");
            Field foreignTablesField = documentation.getField(tableName, columnName, documentation.getGenerationForTable(table).getGenerateIdDataType());
            String tableNameColumnName = "TableName " + tableName + " and columnName " + columnName;
            Assert.isNotNull(foreignTablesField, tableNameColumnName + " does not exist");
            Assert.isEquals(field.getDataType(), foreignTablesField.getDataType(), tableNameColumnName + " has different data types " +
                    "(" + field.getDataType() + " and " + foreignTablesField.getDataType() + ")");
        }
    }

    private void validateIndexes(Table table) {
        if (table.getIndexes() != null && !table.getIndexes().isEmpty()) {
            for (Index index : table.getIndexes()) {
                Assert.isNotNull(index.getName(), "Index without a name in table " + table.getName());
                Assert.isNotNull(index.getFields(), "Index " + index.getName() + " has no fields");
                Assert.isTrue(!index.getFields().isEmpty(), "Index " + index.getName() + " has no fields");
                Set<String> fieldNames = new HashSet<>(index.getFields());
                Assert.isTrue(fieldNames.size() == index.getFields().size(), "Index " + index.getName() + " has duplicate fields");
                for (String field : index.getFields()) {
                    Assert.isNotNull(fieldNames, "Index " + index.getName() + " has empty field names");
                    Field fieldExists = documentation.getField(table.getName(), field, documentation.getGenerationForTable(table).getGenerateIdDataType());
                    Assert.isNotNull(fieldExists, "Index " + index.getName() + " points to non-existing field " + field);
                }
            }
        }
    }

    private void validateViews(Table table) {
        if (table.getViews() != null && !table.getViews().isEmpty()) {
            for (View view : table.getViews()) {
                Assert.isNotNull(view.getName(), "View without a name in table " + table.getName());
                Assert.isNotNull(view.getSql(), "View " + view.getName() + " has no sql");
            }
        }
    }

}
