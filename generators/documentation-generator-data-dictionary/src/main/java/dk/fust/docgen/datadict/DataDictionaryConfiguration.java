package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Configuration for the data dictionary generation
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataDictionaryConfiguration extends AbstractDataDictionaryConfiguration {

    private boolean addDescriptionForFile = false;

    private boolean exportFilename = true;
    private boolean exportColumn = true;
    private boolean exportPosition = true;
    private boolean exportDataType = true;
    private boolean exportMandatory = true;
    private boolean exportKeys = true;
    private boolean exportDescription = true;

    @Override
    public Generator getGenerator() {
        return new DataDictionaryGenerator();
    }

}
