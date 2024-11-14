package dk.fust.docgen.datalineage;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.destination.Destination;
import dk.fust.docgen.format.table.MarkdownTableFormatter;
import dk.fust.docgen.format.table.TableFormatter;
import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for Data Lineage
 */
@Data
public class DataLineageConfiguration implements GeneratorConfiguration {

    private File documentationFile;

    private Destination destination;

    /**
     * Map of other documentation files that you can point to in `source`
     */
    private Map<String, File> sourceDocumentationFiles;

    private String key;

    private TableFormatter tableFormatter = new MarkdownTableFormatter();

    private String filterTags;

    @Override
    public void validate() {
        Assert.isNotNull(documentationFile, "documentationFile must not be null");
        Assert.isNotNull(destination, "destination must not be null");
        Assert.isNotNull(key, "key must not be null");
        Assert.isNotNull(tableFormatter, "tableFormatter must not be null");
    }

    @Override
    public Generator getGenerator() {
        return new DataLineageGenerator();
    }

}
