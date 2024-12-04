package dk.fust.docgen.format.table;

import lombok.Data;
import lombok.Setter;

/**
 * Represent a cell in a table
 */
@Data
public class Cell {

    private int colspan = 1;
    private int rowspan = 1;
    private boolean header = false;
    @Setter
    private String content;
    private Long contentLong;

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
     * Constructor using a long
     * @param contentLong content as long
     */
    public Cell(Long contentLong) {
        this.contentLong = contentLong;
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

    /**
     * Returning a String representation of contentLong if not null,
     * otherwise content
     * @return contentLong or content
     */
    public String getContent() {
        if (contentLong != null) {
            return Long.toString(contentLong);
        }
        return content;
    }

}
