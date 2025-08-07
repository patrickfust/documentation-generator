package dk.fust.docgen.model;

import lombok.Data;

/**
 * Definition of a fields foreign key
 */
@Data
public class ForeignKey {

    @Description(value = "Table name the foreign key points to. Is together with columnName", required = true)
    private String tableName;

    @Description(value = "Column name the foreign key points to. Is together with tableName", required = true)
    private String columnName;

    @Description(value = "Should the database enforce the reference?", hasDefaultBoolean = true, defaultBoolean = false)
    private boolean enforceReference = false;

    @Description(value = "If the referenced record is deleted, what should happen?", defaultValue = "no_action")
    private CascadeAction onDelete = CascadeAction.NO_ACTION;

    @Description(value = "If the referenced record is updated, what should happen?", defaultValue = "no_action")
    private CascadeAction onUpdate = CascadeAction.NO_ACTION;

}
