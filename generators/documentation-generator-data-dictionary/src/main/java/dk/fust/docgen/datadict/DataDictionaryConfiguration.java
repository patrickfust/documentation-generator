package dk.fust.docgen.datadict;

import dk.fust.docgen.Generator;
import dk.fust.docgen.format.table.Alignment;
import dk.fust.docgen.model.annotation.MergeWithDefault;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Configuration for the data dictionary generation
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DataDictionaryConfiguration extends AbstractDataDictionaryConfiguration {

    private boolean addDescriptionForFile = false;

    @MergeWithDefault private DataDictionaryConfigurationColumn columnFilename = new DataDictionaryConfigurationColumn(true, "Filename", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnTable = new DataDictionaryConfigurationColumn(false, "Table", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnColumn = new DataDictionaryConfigurationColumn(true, "Column", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnPosition = new DataDictionaryConfigurationColumn(true, "Position", Alignment.RIGHT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnType = new DataDictionaryConfigurationColumn(true, "Type", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnMandatory = new DataDictionaryConfigurationColumn(true, "Mandatory", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnKeys = new DataDictionaryConfigurationColumn(true, "Keys", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnDescription = new DataDictionaryConfigurationColumn(true, "Description", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnExample = new DataDictionaryConfigurationColumn(true, "Example", Alignment.LEFT);
    @MergeWithDefault private DataDictionaryConfigurationColumn columnSchema = new DataDictionaryConfigurationColumn(false, "Schema", Alignment.LEFT);

    private String schemaName = null;

    private List<DataDictionaryConfigurationColumn> columns;

    @Override
    public Generator getGenerator() {
        return new DataDictionaryGenerator();
    }

}
