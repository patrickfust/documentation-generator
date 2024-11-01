package dk.fust.docgen.sqlscript

import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.DirectoryDestination
import spock.lang.Specification
import spock.lang.Unroll

@Unroll
class SqlScriptGeneratorSpec extends Specification {

    def "create script for #documentationFile"() {
        when:
        SqlScriptConfiguration scriptConfiguration = new SqlScriptConfiguration(
                destination: new DirectoryDestination(directory: new File(directory), createParentDirectories: true),
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

    def "generateId is false"() {
        when:
        File dir = new File('build/test-scripts-generateId')
        SqlScriptConfiguration scriptConfiguration = new SqlScriptConfiguration(
                destination: new DirectoryDestination(directory: dir, createParentDirectories: true),
                documentationFile: new File("src/test/resources/documentation-generateId.yml")
        )
        scriptConfiguration.validate()
        scriptConfiguration.generator.generate(
                TestHelper.loadTestDocumentation("documentation-generateId.yml"),
                scriptConfiguration
        )

        then:
        noExceptionThrown()
        String sql = new File(dir, 'V1__create_table_some_table.sql').text

        and: 'may not have generated an id'
        !sql.contains('some_table_id')
    }
}
