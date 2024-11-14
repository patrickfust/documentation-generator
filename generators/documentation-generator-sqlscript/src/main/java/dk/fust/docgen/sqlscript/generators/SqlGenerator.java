package dk.fust.docgen.sqlscript.generators;

import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.sqlscript.SqlScriptConfiguration;

import java.io.IOException;

/**
 * Generates SQL scripts for a specific database
 */
public interface SqlGenerator {

    /**
     * Generate SQL
     * @param documentation source file with tables, views and indexes
     * @param sqlScriptConfiguration how we want to generation to happen
     * @throws IOException an error occurred
     */
    void generate(Documentation documentation, SqlScriptConfiguration sqlScriptConfiguration) throws IOException;

}
