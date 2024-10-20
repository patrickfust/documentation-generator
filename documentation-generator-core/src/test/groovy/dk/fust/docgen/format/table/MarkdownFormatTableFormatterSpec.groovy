package dk.fust.docgen.format.table

import dk.fust.docgen.TestData
import spock.lang.Specification

class MarkdownFormatTableFormatterSpec extends Specification {

    def "generate table"() {
        given:
        FormatTable table = TestData.generateTable()

        when:
        MarkdownTableFormatter markdownTableFormat = new MarkdownTableFormatter()
        String markdownTable = markdownTableFormat.formatTable(table)

        then:
        markdownTable
    }
}
