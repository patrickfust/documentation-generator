package dk.fust.docgen.sqlscript.generators

import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.DirectoryDestination
import dk.fust.docgen.model.Documentation
import dk.fust.docgen.sqlscript.SqlScriptConfiguration
import spock.lang.Specification

class PostgresGeneratorSpec extends Specification {

    def "generate without indexes"() {
        setup:
        PostgresGenerator generator = new PostgresGenerator()
        String documentationFile = "documentation-sqlscript.yaml"
        Documentation documentation = TestHelper.loadTestDocumentation(documentationFile)
        SqlScriptConfiguration scriptConfiguration = new SqlScriptConfiguration(
                destination: new DirectoryDestination(directory: new File('build/test-scripts-postgres'), createParentDirectories: true),
                documentationFile: new File("src/test/resources/$documentationFile")
        )
        scriptConfiguration.validate()

        when:
        generator.generate(documentation, scriptConfiguration)

        then:
        noExceptionThrown()
    }

    def "generate with index comment"() {
        setup:
        PostgresGenerator generator = new PostgresGenerator()
        String documentationFile = "documentation-sqlscript-with-index-comment.yaml"
        Documentation documentation = TestHelper.loadTestDocumentation(documentationFile)
        File outputDirectory = new File('build/test-scripts-postgres-index-comment')
        SqlScriptConfiguration scriptConfiguration = new SqlScriptConfiguration(
                destination: new DirectoryDestination(directory: outputDirectory, createParentDirectories: true),
                documentationFile: new File("src/test/resources/$documentationFile")
        )
        scriptConfiguration.validate()

        when:
        generator.generate(documentation, scriptConfiguration)

        then:
        noExceptionThrown()
        String indexFileText = new File(outputDirectory, 'create-index_with_comment.sql').text
        indexFileText.contains("comment on index xxx.nonUniqueIdx is 'a comment on nonUniqueIdx';")
        indexFileText.contains("comment on index xxx.uniqueIdx is 'a comment on uniqueIdx';")
    }
}
