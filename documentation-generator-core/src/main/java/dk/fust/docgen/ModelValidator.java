package dk.fust.docgen;

import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.model.Field;
import dk.fust.docgen.model.Table;
import dk.fust.docgen.util.Assert;

/**
 * Validates that the model of the documentation is in order
 */
public class ModelValidator {

    private final Documentation documentation;

    /**
     * Constructor with the documentation to validate
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

}
