package dk.fust.docgen.confluence.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * Confluence page
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Page {

    private String id;
    private String status;
    private String title;
    private String type;
    private Body body;
    private Version version;

    // Child pages
    private List<Page> results;

    private List<Page> ancestors;

    private Space space;

    private Metadata metadata;

}
