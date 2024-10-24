package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Confluence space
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Space {

    private String key;

    /**
     * Constructor
     * @param key key
     */
    public Space(String key) {
        this.key = key;
    }

    /**
     * Constructor
     */
    public Space() {
    }
}
