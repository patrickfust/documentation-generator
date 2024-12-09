package dk.fust.docgen.csv.format.table;

/**
 * Type of delimiter when generating csv-files
 */
public enum CSVDelimiter {

    /**
     * Use ',' as delimiter
     */
    COMMA(","),

    /**
     * Use ';' as delimiter
     */
    SEMICOLON(";"),

    /**
     * Use '|' as delimiter
     */
    PIPE("|"),

    /**
     * Use ' ' as delimiter
     */
    SPACE(" "),

    /**
     * Use 'tab' as delimiter
     */
    TAB("\t");

    CSVDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    private final String delimiter;

    /**
     * @return the actual delimiter character
     */
    public String getDelimiter() {
        return delimiter;
    }

}
