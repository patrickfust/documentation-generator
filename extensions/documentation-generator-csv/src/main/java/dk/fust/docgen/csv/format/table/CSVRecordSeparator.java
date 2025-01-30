package dk.fust.docgen.csv.format.table;

/**
 * Type of record separator when generating csv-files
 */
public enum CSVRecordSeparator {

    /**
     * Use '\n' as record separator
     */
    NEWLINE("\n"),

    /**
     * Use '\r\n' as record separator
     */
    CARRIAGE_RETURN_NEWLINE("\r\n"),

    /**
     * Use '\r' as record separator
     */
    CARRIAGE_RETURN("\r");

    CSVRecordSeparator(String recordSeparator) {
        this.recordSeparator = recordSeparator;
    }

    private final String recordSeparator;

    /**
     * @return the actual record separator
     */
    public String getRecordSeparator() {
        return recordSeparator;
    }
}
