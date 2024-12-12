package dk.fust.docgen.util;

/**
 * Helper class used verify state of configurations and assertions
 */
public class Assert {

    /**
     * Verifies that `expression` is evaluated to `true`.
     * Otherwise is an `IllegalArgumentException` thrown.
     * @param expression must evaluate to true
     * @param message message in `IllegalArgumentException` that is thrown if expression is false
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Verifies that `expression` is evaluated to `false`.
     * Otherwise is an `IllegalArgumentException` thrown.
     * @param expression must evaluate to false
     * @param message message in `IllegalArgumentException` that is thrown if expression is true
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Verifies that `object` is not null
     * Otherwise is an `IllegalArgumentException` thrown.
     * @param object object to verify
     * @param message message in `IllegalArgumentException` that is thrown if object is null
     */
    public static void isNotNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Verifies that `object` is null
     * Otherwise is an `IllegalArgumentException` thrown.
     * @param object object to verify
     * @param message message in `IllegalArgumentException` that is thrown if object is not null
     */
    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Verifies that o1 and o2 er considered equal.
     * Otherwise is an `IllegalArgumentException` thrown.
     * @param o1 first object
     * @param o2 second object
     * @param message message in `IllegalArgumentException` that is thrown if the object are not equal
     */
    public static void isEquals(Object o1, Object o2, String message) {
        if (o1 == null || !o1.equals(o2)) {
            throw new IllegalArgumentException(message);
        }
    }

}
