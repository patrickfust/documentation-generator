package dk.fust.docgen.model;

import lombok.Data;

/**
 * Definition of a fields foreign key
 */
@Data
public class ForeignKey {

    private String tableName;
    private String columnName;

    @Description(value = "Should the database enforce the reference?", hasDefaultBoolean = true, defaultBoolean = false)
    private boolean enforceReference = false;

}
