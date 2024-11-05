package dk.fust.docgen.model.datadict;

import dk.fust.docgen.model.Description;
import lombok.Data;

import java.util.List;

/**
 * Data Dictionary File
 */
@Data
public class File {

    @Description(value = "Filename for the data dictionary", required = true)
    private String fileName;

    @Description(value = "Data dictionary version", required = true)
    private String version;

    @Description(value = "The files columns", required = true)
    private List<Column> columns;
}
