package dk.fust.docgen.model;

import lombok.Data;

import java.util.List;

/**
 * Definition af an index
 */
@Data
public class Index {

    private String name;
    private boolean unique = false;
    private List<String> fields;

}
