package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.format.table.FormatTable;
import dk.fust.docgen.model.Documentation;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Common generator functionality for data dictionaries
 */
@Slf4j
public abstract class AbstractDataDictionaryGenerator implements Generator {

    /**
     * Generate the FormatTable for the data dictionary
     * @param documentation model of the data dictionary
     * @param configuration configuration
     * @return a table containing the relevant information
     */
    protected abstract FormatTable generateTable(Documentation documentation, AbstractDataDictionaryConfiguration configuration);

    @Override
    public void generate(Documentation documentation, GeneratorConfiguration generatorConfiguration) throws IOException {
        AbstractDataDictionaryConfiguration dataDictionaryConfiguration = (AbstractDataDictionaryConfiguration) generatorConfiguration;
        if (documentation.getDataDictionary() != null && documentation.getDataDictionary().getDataDictionaryFiles() != null) {
            sendTableToDestination(generateTable(documentation, dataDictionaryConfiguration), dataDictionaryConfiguration);
        }
    }

    private void sendTableToDestination(FormatTable formatTable, AbstractDataDictionaryConfiguration configuration) throws IOException {
        String table = configuration.getTableFormatter().formatTable(formatTable);
        configuration.getDestination().sendDocumentToDestination(table, configuration.getKey());
    }

}
