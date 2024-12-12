package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import lombok.EqualsAndHashCode;

/**
 * Configuration for generating meta information for data dictionaries
 */
@EqualsAndHashCode(callSuper = true)
public class DataDictionaryMetaInfoConfiguration extends AbstractDataDictionaryConfiguration {

    @Override
    public Generator getGenerator() {
        return new DataDictionaryMetaInfoGenerator();
    }

}
