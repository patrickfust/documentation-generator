package dk.fust.docgen.sqlscript.generators;

/**
 * Factory to choose the correct generator from
 */
public class SqlGeneratorFactory {

    /**
     * Get the right generator for the specific dialoect
     * @param sqlDialect dialect
     * @return the concrete generator
     */
    public static SqlGenerator getSqlGenerator(SqlDialect sqlDialect) {
        return switch (sqlDialect) {
            case POSTGRES -> new PostgresGenerator();
        };

    }
}
