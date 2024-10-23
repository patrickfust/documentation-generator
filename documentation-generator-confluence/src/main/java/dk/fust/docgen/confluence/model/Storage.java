package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Confluence storage
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Storage {

    private String value;
    private String representation;

    /**
     * Constructor
     * @param value value
     * @param representation representation
     */
    public Storage(String value, String representation) {
        this.value = value;
        this.representation = representation;
    }

    /**
     * Constructor
     */
    public Storage() {
    }
}
