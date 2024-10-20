package dk.fust.docgen.format.table;

import lombok.Data;

/**
 * Represent a Col that can have background colors
 */
@Data
public class Col {

    /**
     * Constructor
     */
    public Col() {
    }

    /**
     * Constructor
     * @param colspan colspan
     */
    public Col(int colspan) {
        this.colspan = colspan;
    }

    private int colspan = 1;

    private String backgroundColor;

}
