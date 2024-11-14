package dk.fust.docgen.model;

import lombok.Data;

/**
 * Definition of a view
 */
@Data
public class View {

    @Description(value = "Name of the view")
    private String name;

    @Description(value = "SQL that defines the view", required = true)
    private String sql;

}
