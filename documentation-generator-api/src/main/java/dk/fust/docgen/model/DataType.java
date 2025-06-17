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
     * Array of integers
     */
    INT_ARRAY,

    /**
     * Date
     */
    DATE,

    /**
     * Array of dates
     */
    DATA_ARRAY,

    /**
     * Timestamp
     */
    TIMESTAMPTZ,

    /**
     * Array of timestamps
     */
    TIMESTAMPTZ_ARRAY,

    /**
     * Numeric
     */
    NUMERIC,

    /**
     * Array of numerics
     */
    NUMERIC_ARRAY,

    /**
     * Boolean
     */
    BOOL,

    /**
     * Array of booleans
     */
    BOOL_ARRAY,

    /**
     * Big integer
     */
    BIGINT,

    /**
     * Array of big integers
     */
    BIGINT_ARRAY,

    /**
     * Text (String)
     */
    TEXT,

    /**
     * Array of text (String)
     */
    TEXT_ARRAY,

    /**
     * UUID
     */
    UUID,

    /**
     * Array of UUIDs
     */
    UUID_ARRAY;

    /**
     * We want it to be in lower case when documenting
     * @return the enum in lower case
     */
    public String toLowerCase() {
        return name().toLowerCase().replace("_array", "[]");
    }
}
