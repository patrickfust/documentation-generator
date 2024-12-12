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
    private boolean exportTableName = false;
    private boolean exportColumn = true;
    private boolean exportPosition = true;
    private boolean exportDataType = true;
    private boolean exportMandatory = true;
    private boolean exportKeys = true;
    private boolean exportDescription = true;
    private boolean exportExample = true;
    private boolean exportSchema = false;

    private String headerSchema = "Schema";
    private String headerFilename = "Filename";
    private String headerTableName = "Table";
    private String headerColumn = "Column";
    private String headerPosition = "Position";
    private String headerDataType = "Type";
    private String headerMandatory = "Mandatory";
    private String headerKeys = "Keys";
    private String headerDescription = "Description";
    private String headerExample = "Example";

    private String schemaName = null;

    @Override
    public Generator getGenerator() {
        return new DataDictionaryGenerator();
    }

}
