package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.destination.Destination;
import dk.fust.docgen.format.table.MarkdownTableFormatter;
import dk.fust.docgen.format.table.TableFormatter;
import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;

/**
 * Configuration for the data dictionary generation
 */
@Data
public class DataDictionaryConfiguration implements GeneratorConfiguration {

    private File documentationFile;

    private Destination destination;

    private String key;

    private TableFormatter tableFormatter = new MarkdownTableFormatter();

    private boolean addDescriptionForFile = false;

    @Override
    public void validate() {
        Assert.isNotNull(destination, "Destination is required");
        Assert.isNotNull(documentationFile, "Documentation file is required");
        Assert.isNotNull(tableFormatter, "TableFormatter is required");
    }

    @Override
    public Generator getGenerator() {
        return new DataDictionaryGenerator();
    }

}
