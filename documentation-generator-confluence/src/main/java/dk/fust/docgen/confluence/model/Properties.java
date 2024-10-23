package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * Confluence metadata properties
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Properties {

    @JsonProperty("content-appearance-draft")
    private PropertyValue contentAppearanceDraft;

    @JsonProperty("content-appearance-published")
    private PropertyValue contentAppearancePublished;

}
