package dk.fust.docgen.format.table;

import lombok.Data;
import lombok.Setter;

/**
 * Represent a cell in a table
 */
@Data
public class Cell {

    private int colspan = 1;
    private boolean header = false;
    @Setter
    private String content;
    private Long contentLong;
    private Alignment alignment = Alignment.LEFT;

    /**
     * Default constructor
     */
    public Cell() {
    }

    /**
     * Constructor
     * @param colspan colspan
     * @param content content
     * @param header true if it's a header
     */
    public Cell(int colspan, String content, boolean header) {
        this.colspan = colspan;
        this.header = header;
        this.content = content;
    }

    /**
     * Constructor
     * @param colspan colspan
     * @param content content
     * @param header true if it's a header
     * @param alignment alignment
     */
    public Cell(int colspan, String content, boolean header, Alignment alignment) {
        this.colspan = colspan;
        this.header = header;
        this.content = content;
        this.alignment = alignment;
    }

    /**
     * Constructor
     * @param colspan colspan
     * @param content content
     */
    public Cell(int colspan, String content) {
        this.colspan = colspan;
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

    /**
     * Returns the size of the content
     * @return size of the content
     */
    public int getContentSize() {
        String c = getContent();
        if (c == null) {
            return 0;
        }
        return c.length();
    }
}
