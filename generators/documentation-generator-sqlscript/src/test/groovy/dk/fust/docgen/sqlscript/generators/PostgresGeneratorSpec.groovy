package dk.fust.docgen.sqlscript.generators

import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.DirectoryDestination
import dk.fust.docgen.model.Documentation
import dk.fust.docgen.sqlscript.SqlScriptConfiguration
import spock.lang.Specification

class PostgresGeneratorSpec extends Specification {

    def "generate without indexes"() {
        when:
        generate('documentation-sqlscript.yaml', 'build/test-scripts-postgres')

        then:
        noExceptionThrown()
    }

    def "generate with index comment"() {
        setup:
        String outputDirectory = 'build/test-scripts-postgres-index-comment'

        when:
        generate('documentation-sqlscript-with-index-comment.yaml', outputDirectory)

        then:
        noExceptionThrown()
        String indexFileText = new File(outputDirectory, 'create-index_with_comment.sql').text
        indexFileText.contains("comment on index xxx.nonUniqueIdx is 'a comment on nonUniqueIdx';")
        indexFileText.contains("comment on index xxx.uniqueIdx is 'a comment on uniqueIdx';")
    }

    def "generate with integer array"() {
        setup:
        String outputDirectory = 'build/test-scripts-postgres-integer-array'

        when:
        generate('documentation-sqlscript-integer-array.yaml', outputDirectory)

        then:
        noExceptionThrown()
        String indexFileText = new File(outputDirectory, 'create-something-with-integer-array.sql').text
        indexFileText.contains("name int[]")
    }

    def "generate with foreign keys"() {
        setup:
        String outputDirectory = 'build/test-scripts-postgres-foreign-keys'

        when:
        generate('documentation-sqlscript-foreign-keys.yaml', outputDirectory)

        then:
        noExceptionThrown()
        new File(outputDirectory, 'plain.sql').text.contains("parent text references parent_table(key),")
        new File(outputDirectory, 'delete_cascade.sql').text.contains("parent text references parent_table(key) on delete cascade,")
        new File(outputDirectory, 'update_cascade.sql').text.contains("parent text references parent_table(key) on update cascade,")
        new File(outputDirectory, 'mixed.sql').text.contains("parent text references parent_table(key) on update set null on delete set default,")
    }

    def "generate with collate"() {
        setup:
        String outputDirectory = 'build/test-scripts-collate'

        when:
        generate('documentation-sqlscript-collate.yaml', outputDirectory)

        then:
        noExceptionThrown()
        String someTable = new File(outputDirectory, 'some_table.sql').text
        someTable.contains('field_with_default_collate text,')
        someTable.contains('field_with_specific_collate text collate "C"')
    }

    private void generate(String documentationFile, String outputDirectory) {
        PostgresGenerator generator = new PostgresGenerator()
        Documentation documentation = TestHelper.loadTestDocumentation(documentationFile)
        SqlScriptConfiguration scriptConfiguration = new SqlScriptConfiguration(
                destination: new DirectoryDestination(directory: new File(outputDirectory), createParentDirectories: true),
                documentationFile: new File("src/test/resources/$documentationFile")
        )
        scriptConfiguration.validate()
        generator.generate(documentation, scriptConfiguration)
    }

}
