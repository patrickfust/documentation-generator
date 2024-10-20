package dk.fust.docgen.model;

import lombok.Data;

/**
 * Information about how fields are generated
 */
@Data
public class Generation {

    @Description(value = "Type of the generated id of the table", defaultValue = "int")
    private DataType generateIdDataType;

    @Description("Automatically generate id on the form `tablename_id`")
    private Boolean generateId;

    @Description("Adds `columnNameCreatedAt` column, that is populated with `now()`")
    private Boolean addCreatedAt;

    @Description(value = "If `addCreatedAt`, this will be the columnName", defaultValue = "created_at")
    private String columnNameCreatedAt;

    @Description("Adds `columnNameUpdatedAt` column, that is populated with `now()`")
    private Boolean addUpdatedAt;

    @Description(value = "If `addUpdatedAt`, this will be the columnName", defaultValue = "updated_at")
    private String columnNameUpdatedAt;

    @Description(value = "If this is non-empty, a trigger will be called with this name", defaultValue = "")
    private String triggerForUpdates;

    /**
     * Has specified generateId. See generateId
     * @return true if generateId is present
     */
    public boolean hasGenerateId() {
        return generateId != null;
    }

    /**
     * Will generate ID
     * @return true if we will generate an id
     */
    public boolean isGenerateId() {
        return hasGenerateId() && generateId;
    }

}
