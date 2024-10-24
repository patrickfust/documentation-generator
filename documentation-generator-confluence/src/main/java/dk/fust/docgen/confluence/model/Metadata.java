package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Confluence Metadata
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Metadata {

    private Properties properties;

    /**
     * Create a standard metadata with full size page
     * @return standard metadata
     */
    public static Metadata fullPageWidthMetadata() {
        Metadata metadata = new Metadata();
        Properties properties = new Properties();
        properties.setContentAppearanceDraft(PropertyValue.fullWithPropertyValue());
        properties.setContentAppearancePublished(PropertyValue.fullWithPropertyValue());
        metadata.properties = properties;
        return metadata;
    }
}
