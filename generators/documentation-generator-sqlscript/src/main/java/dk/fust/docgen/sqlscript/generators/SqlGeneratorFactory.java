package dk.fust.docgen.sqlscript.generators;

/**
 * Factory to choose the correct generator from
 */
public class SqlGeneratorFactory {

    public static SqlGenerator getSqlGenerator(SqlDialect sqlDialect) {
        return switch (sqlDialect) {
            case POSTGRES -> new PostgresGenerator();
        };

    }
}
