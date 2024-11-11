package dk.fust.docgen.sqlscript.generators;

import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.sqlscript.SqlScriptConfiguration;

import java.io.IOException;

public interface SqlGenerator {

    void generate(Documentation documentation, SqlScriptConfiguration sqlScriptConfiguration) throws IOException;

}
