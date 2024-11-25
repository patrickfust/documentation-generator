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

    private String filterTags;

    private TableFormatter tableFormatter = new MarkdownTableFormatter();

    private boolean addDescriptionForFile = false;

    private boolean exportFilename = true;
    private boolean exportColumn = true;
    private boolean exportPosition = true;
    private boolean exportDataType = true;
    private boolean exportMandatory = true;
    private boolean exportKeys = true;
    private boolean exportDescription = true;

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
