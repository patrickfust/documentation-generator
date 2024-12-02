package dk.fust.docgen.format.table;

import lombok.Data;

/**
 * Represent a cell in a table
 */
@Data
public class Cell {

    private int colspan = 1;
    private int rowspan = 1;
    private boolean header = false;
    private String content;

    /**
     * Default constructor
     */
    public Cell() {
    }

    /**
     * Constructor
     * @param colspan colspan
     * @param rowspan rowspan
     * @param content content
     * @param header true if it's a header
     */
    public Cell(int colspan, int rowspan, String content, boolean header) {
        this.colspan = colspan;
        this.rowspan = rowspan;
        this.header = header;
        this.content = content;
    }

    /**
     * Constructor
     * @param colspan colspan
     * @param rowspan rowspan
     * @param content content
     */
    public Cell(int colspan, int rowspan, String content) {
        this.colspan = colspan;
        this.rowspan = rowspan;
        this.content = content;
    }

    /**
     * Constructor
     * @param content content
     */
    public Cell(String content) {
        this.content = content;
    }

    /**
     * Constructor
     * @param content content
     * @param header true if it's a header
     */
    public Cell(String content, boolean header) {
        this.content = content;
        this.header = header;
    }
}
