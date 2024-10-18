package dk.fust.docgen.format.table

import dk.fust.docgen.TestData
import spock.lang.Specification

class MarkdownTableFormatSpec extends Specification {

    def "generate table"() {
        given:
        Table table = TestData.generateTable()

        when:
        MarkdownTableFormat markdownTableFormat = new MarkdownTableFormat()
        String markdownTable = markdownTableFormat.generateTable(table)

        then:
        markdownTable
    }
}
