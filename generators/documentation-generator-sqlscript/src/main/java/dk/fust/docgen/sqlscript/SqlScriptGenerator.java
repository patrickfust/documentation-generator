package dk.fust.docgen.sqlscript;

import dk.fust.docgen.Generator;
import dk.fust.docgen.GeneratorConfiguration;
import dk.fust.docgen.model.Documentation;
import dk.fust.docgen.sqlscript.generators.SqlGenerator;
import dk.fust.docgen.sqlscript.generators.SqlGeneratorFactory;
import dk.fust.docgen.util.Assert;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * Generator for SQL files
 */
@Slf4j
@Data
public class SqlScriptGenerator implements Generator {

    @Override
    public void generate(Documentation documentation, GeneratorConfiguration generatorConfiguration) throws IOException {
        log.info("Generating SQL-scripts...");
        Assert.isTrue(generatorConfiguration instanceof SqlScriptConfiguration, "configuration must be an instance of SqlScriptConfiguration");
        SqlScriptConfiguration sqlScriptConfiguration = (SqlScriptConfiguration) generatorConfiguration;
        SqlGenerator sqlGenerator = SqlGeneratorFactory.getSqlGenerator(sqlScriptConfiguration.getSqlDialect());
        sqlGenerator.generate(documentation, sqlScriptConfiguration);
    }

}
