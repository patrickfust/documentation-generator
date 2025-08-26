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

    @Description("If the field is transformed, this is the documentation on how it's transformed")
    private String transformation;

    @Description("Points to another table and field. Must be on the form 'table.field'")
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
    private List<String> check;

    @Description("How to collate the field. Omitted means use database default collation")
    private String collate;

}
