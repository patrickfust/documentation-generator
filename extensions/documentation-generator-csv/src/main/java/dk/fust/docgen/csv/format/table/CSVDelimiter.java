package dk.fust.docgen.csv.format.table;

public enum CSVDelimiter {

    COMMA(","),
    SEMICOLON(";"),
    PIPE("|"),
    SPACE(" "),
    TAB("\t");

    CSVDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    private final String delimiter;

    public String getDelimiter() {
        return delimiter;
    }

}
