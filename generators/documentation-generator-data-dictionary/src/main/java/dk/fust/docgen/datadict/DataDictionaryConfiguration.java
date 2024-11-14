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

    @Override
    public Generator getGenerator() {
        return new DataDictionaryGenerator();
    }

}
