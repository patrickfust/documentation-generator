package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Confluence value for a property
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PropertyValue {

    private String value;

    /**
     * Standard property for creation of a full page width
     * @return standard property values
     */
    public static PropertyValue fullWithPropertyValue() {
        PropertyValue propertyValue = new PropertyValue();
        propertyValue.setValue("full-width");
        return propertyValue;
    }

}
