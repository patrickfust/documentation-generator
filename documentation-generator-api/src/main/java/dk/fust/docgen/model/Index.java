package dk.fust.docgen.model;

import lombok.Data;

import java.util.List;

/**
 * Definition af an index
 */
@Data
public class Index {

    @Description("Name of the index")
    private String name;

    private boolean unique = false;

    @Description(value = "List of fields for the index", required = true)
    private List<String> fields;

}
