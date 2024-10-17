package dk.fust.docgen.model;

import lombok.Data;

import java.util.List;

/**
 * Definition of a field
 */
@Data
public class Field {

    @Description(value = "Name of the column", required = true)
    private String name;
    private String comment;
    private String transformation;
    private String source;

    @Description(value = "Data type of the column", required = true)
    private DataType dataType;

    @Description("Is this a foreign key to another table")
    private ForeignKey foreignKey;
    private boolean primaryKey = false;
    private boolean unique = false;

    @Description(value = "May the field be null?", hasDefaultBoolean = true, defaultBoolean = true)
    private boolean nullable = true;

    @Description("Default value when inserting in the database")
    private String defaultValue;

    @Description("Constraint on the values")
    List<String> check;

}
