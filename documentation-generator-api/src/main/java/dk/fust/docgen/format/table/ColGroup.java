package dk.fust.docgen.format.table;

import lombok.Data;

import java.util.List;

/**
 * ColGroup for at table
 */
@Data
public class ColGroup {

    private List<Col> cols;

}
