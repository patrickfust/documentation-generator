package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Confluence body
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Body {

    private Storage storage;

    /**
     * Constructor
     * @param storage storage
     */
    public Body(Storage storage) {
        this.storage = storage;
    }

    /**
     * Constructor
     */
    public Body() {
    }
}
