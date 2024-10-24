package dk.fust.docgen.sqlscript;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.destination.Destination;
import dk.fust.docgen.util.Assert;
import lombok.Data;

import java.io.File;

/**
 * Configuration for generating SQL scripts
 */
@Data
public class SqlScriptConfiguration implements GeneratorConfiguration {

    private File documentationFile;

    private Destination destination;

    @Override
    public void validate() {
        Assert.isNotNull(documentationFile, "documentationFile must not be null");
        Assert.isNotNull(destination, "destination must not be null");
        destination.validate();
    }

    @Override
    public Generator getGenerator() {
        return new SqlScriptGenerator();
    }

}
