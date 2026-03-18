package dk.fust.docgen.model;

import lombok.Data;

/**
 * Used for linking between referencing table and reference table-
 */
@Data
public class CombinedForeignKeyColumn {

    @Description(value = "Column name in the current table that is referencing the other table", required = true)
    private String referencingColumn;

    @Description(value = "Column name in the reference table that is referenced from the current table", required = true)
    private String referenceColumn;

}
