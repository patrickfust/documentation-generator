package dk.fust.docgen.model;

/**
 * Data types
 */
public enum DataType {

    /**
     * Integer
     */
    INT,

    /**
     * Date
     */
    DATE,

    /**
     * Timestamp
     */
    TIMESTAMPTZ,

    /**
     * Numeric
     */
    NUMERIC,

    /**
     * Boolean
     */
    BOOL,

    /**
     * Big integer
     */
    BIGINT,

    /**
     * Text (String)
     */
    TEXT,

    /**
     * UUID
     */
    UUID;

    /**
     * We want it to be in lower case when documenting
     * @return the enum in lower case
     */
    public String toLowerCase() {
        return name().toLowerCase();
    }
}
