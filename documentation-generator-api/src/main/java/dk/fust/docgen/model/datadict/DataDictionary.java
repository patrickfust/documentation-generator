package dk.fust.docgen.model.datadict;

import dk.fust.docgen.model.Description;
import lombok.Data;

import java.util.List;

/**
 * Data Dictionary
 */
@Data
public class DataDictionary {

    @Description(value = "List of data dictionary files")
    private List<DataDictionaryFile> dataDictionaryFiles;

}
