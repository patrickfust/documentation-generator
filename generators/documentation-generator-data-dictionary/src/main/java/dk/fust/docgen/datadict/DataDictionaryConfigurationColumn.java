package dk.fust.docgen.datadict;

import dk.fust.docgen.format.table.Alignment;
import lombok.Data;

/**
 * Configuration for the data dictionary columns
 */
@Data
public class DataDictionaryConfigurationColumn {

    private Boolean export;
    private String header;
    private Alignment alignment;

    /**
     * Default constructor
     */
    public DataDictionaryConfigurationColumn() {
    }

    /**
     * Constructor
     * @param export should the column be exported
     * @param header the header of the column
     * @param alignment alignment
     */
    public DataDictionaryConfigurationColumn(boolean export, String header, Alignment alignment) {
        this.export = export;
        this.header = header;
        this.alignment = alignment;
    }

}
