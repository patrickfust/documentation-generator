package dk.fust.docgen.model;

import lombok.Data;

import java.util.List;

/**
 * Used when a combination of fields consists of a foreign key
 */
@Data
public class CombinedForeignKey {

    @Description(value = "Reference table name the foreign key points to. Is together with columnNames", required = true)
    private String tableName;

    @Description(value = "Columns between this table and the reference table", required = true)
    private List<CombinedForeignKeyColumn> columns;

    @Description(value = "Should the database enforce the reference?", hasDefaultBoolean = true, defaultBoolean = false)
    private boolean enforceReference = false;

    @Description(value = "If the referenced record is deleted, what should happen?", defaultValue = "no_action")
    private CascadeAction onDelete = CascadeAction.NO_ACTION;

    @Description(value = "If the referenced record is updated, what should happen?", defaultValue = "no_action")
    private CascadeAction onUpdate = CascadeAction.NO_ACTION;

}
