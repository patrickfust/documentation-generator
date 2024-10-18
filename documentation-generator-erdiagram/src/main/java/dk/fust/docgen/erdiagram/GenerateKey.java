package dk.fust.docgen.erdiagram;

import lombok.Data;

/**
 * Filter for choosing specific tables in a group and where to replace it in the markdown
 */
@Data
public class GenerateKey {

    private String group;
    private String destinationKey;

}
