package dk.fust.docgen.sqlscript

import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.FileDestination
import spock.lang.Specification
import spock.lang.Unroll

import java.nio.file.Files

@Unroll
class SqlScriptGeneratorSpec extends Specification {

    def "create script for #documentationFile"() {
        when:
        SqlScriptConfiguration scriptConfiguration = new SqlScriptConfiguration(
                destination: new FileDestination(directory: new File(directory), createParentDirectories: true),
                documentationFile: new File("src/test/resources/${documentationFile}")
        )
        scriptConfiguration.validate()
        scriptConfiguration.generator.generate(
                TestHelper.loadTestDocumentation(documentationFile),
                scriptConfiguration
        )

        then:
        noExceptionThrown()

        where:
        directory                      | documentationFile
        'build/test-scripts'           | 'documentation-sqlscript.yaml'
        'build/test-scripts-no-schema' | 'documentation-sqlscript-no-schema.yaml'
    }

}
