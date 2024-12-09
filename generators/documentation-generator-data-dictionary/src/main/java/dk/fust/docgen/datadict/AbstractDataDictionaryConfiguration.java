package dk.fust.docgen.datadict;

import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.destination.Destination;
import dk.fust.docgen.format.table.MarkdownTableFormatter;
import dk.fust.docgen.format.table.TableFormatter;
import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;

/**
 * Common information for data dictionaries
 */
@Data
public abstract class AbstractDataDictionaryConfiguration implements GeneratorConfiguration {

    private File documentationFile;

    private Destination destination;

    private String key;

    private String filterTags;

    private TableFormatter tableFormatter = new MarkdownTableFormatter();

    @Override
    public void validate() {
        Assert.isNotNull(destination, "Destination is required");
        Assert.isNotNull(documentationFile, "Documentation file is required");
        Assert.isNotNull(tableFormatter, "TableFormatter is required");
    }

}
