package dk.fust.docgen.model;

/**
 * Operation to perform when a referenced record is deleted or updated.
 */
public enum CascadeAction {

    /**
     * Deletes or updates child rows automatically when the parent row is deleted or updated
     */
    CASCADE,

    /**
     * Sets the foreign key column in child rows to NULL when the parent is deleted or updated
     */
    SET_NULL,

    /**
     * Sets the foreign key column in child rows to its default value when the parent key is deleted or updated
     */
    SET_DEFAULT,

    /**
     * Similar to restrict: the update is not allowed if child rows exist. This means no action is generated in the SQL
     */
    NO_ACTION,

    /**
     * Prevents updating the parent key or delete it, if child rows exist
     */
    RESTRICT;

}
