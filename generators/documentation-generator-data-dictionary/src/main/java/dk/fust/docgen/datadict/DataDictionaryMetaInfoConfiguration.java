package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;

/**
 * Configuration for generating meta information for data dictionaries
 */
public class DataDictionaryMetaInfoConfiguration extends AbstractDataDictionaryConfiguration {

    @Override
    public Generator getGenerator() {
        return new DataDictionaryMetaInfoGenerator();
    }

}
