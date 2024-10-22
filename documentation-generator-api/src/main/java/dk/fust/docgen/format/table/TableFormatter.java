package dk.fust.docgen.format.table;

/**
 * How do you generate a table?
 * This could be in the form of HTML or Markdown for instance
 */
public interface TableFormatter {

    /**
     * Convert the table to a string that represent the table
     * @param formatTable table to convert
     * @return a string representing the table
     */
    String formatTable(FormatTable formatTable);

}
