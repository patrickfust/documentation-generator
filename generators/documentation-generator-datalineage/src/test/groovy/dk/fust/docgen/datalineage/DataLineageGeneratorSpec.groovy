package dk.fust.docgen.datalineage

import dk.fust.docgen.TestHelper
import dk.fust.docgen.destination.MarkdownDestination
import dk.fust.docgen.destination.MockDestination
import dk.fust.docgen.format.table.MarkdownTableFormatter
import dk.fust.docgen.format.table.MockTableFormatter
import spock.lang.Specification

class DataLineageGeneratorSpec extends Specification {

    def "generate data lineage"() {
        given:
        MockDestination mockDestination = new MockDestination()
        MockTableFormatter mockTableFormatter = new MockTableFormatter(formattet: 'X')
        DataLineageConfiguration configuration = new DataLineageConfiguration(
                key: "myTableClass",
                destination: mockDestination,
                tableFormatter: mockTableFormatter
        )

        DataLineageGenerator generator = new DataLineageGenerator();

        when:
        generator.generate(TestHelper.loadTestDocumentation("documentation-datalineage.yaml"), configuration);

        then:
        mockDestination.destination == 'myTableClass'
        mockTableFormatter.formatTableArgument.tableClass == 'myTableClass'
        mockTableFormatter.formatTableArgument.rows.size() == 6
    }

    def 'generate as markdown'() {
        given:
        File markdownFile = new File('build/tmp/something.md')
        if (markdownFile.exists()) {
            markdownFile.delete()
        }
        markdownFile << """
[//]: #my-data-lineage_START ()
[//]: #my-data-lineage_END ()
"""
        MarkdownTableFormatter markdownTableFormatter = new MarkdownTableFormatter()
        DataLineageConfiguration conf = new DataLineageConfiguration(
                key: 'my-data-lineage',
                destination: new MarkdownDestination(
                        file: markdownFile,
                ),
                tableFormatter: markdownTableFormatter
        )
        DataLineageGenerator generator = new DataLineageGenerator();

        when:
        generator.generate(TestHelper.loadTestDocumentation("documentation-datalineage.yaml"), conf)
        String markdownContent = markdownFile.text

        then:
        markdownContent.contains '--' // The header
    }

    def 'generate as markdown with external source'() {
        given:
        File markdownFile = new File('build/tmp/something-external.md')
        if (markdownFile.exists()) {
            markdownFile.delete()
        }
        markdownFile << """
[//]: #my-data-lineage_START ()
[//]: #my-data-lineage_END ()
"""
        MarkdownTableFormatter markdownTableFormatter = new MarkdownTableFormatter()
        DataLineageConfiguration conf = new DataLineageConfiguration(
                key: 'my-data-lineage',
                destination: new MarkdownDestination(
                        file: markdownFile,
                ),
                tableFormatter: markdownTableFormatter,
                sourceDocumentationFiles: ['external' : TestHelper.getTestFile('documentation-datalineage-external-source.yaml')]
        )
        DataLineageGenerator generator = new DataLineageGenerator();

        when:
        generator.generate(TestHelper.loadTestDocumentation("documentation-datalineage-with-external-source.yaml"), conf)
        String markdownContent = markdownFile.text

        then:
        markdownContent.contains '--' // The header
    }
}