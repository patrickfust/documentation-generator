package dk.fust.docgen.format.table

import dk.fust.docgen.TestData
import spock.lang.Specification

class MarkdownFormatTableFormatterSpec extends Specification {

    def "find max col size"() {
        when:
        MarkdownTableFormatter markdownTableFormat = new MarkdownTableFormatter()
        int[] sizes = markdownTableFormat.findMaxColSizes(TestData.generateTable())

        then:
        sizes == [14, 11, 9, 14, 10, 11, 9, 19] as int[]
    }

    def "generate table"() {
        when:
        MarkdownTableFormatter markdownTableFormat = new MarkdownTableFormatter()
        String markdownTable = markdownTableFormat.formatTable(TestData.generateTable())

        then:
        noExceptionThrown()
        markdownTable == """|| With colspan 3 |             |           | With colspan 4 |            |             |           | No colspan          |
                            ||----------------|-------------|-----------|----------------|------------|-------------|-----------|---------------------|
                            || Table Name     | Column Name | Data Type | Database Name  | Table Name | Column Name | Data Type |                     |
                            || table_a        | column_a    | int       | database name  | table_b    | column_b    | text      | some transformation |
                            |""".stripMargin().stripIndent()
    }

    def "generate simple table"() {
        given:
        FormatTable table = TestData.generateSimpleTable()

        when:
        String markdownTable = new MarkdownTableFormatter().formatTable(table)

        then:
        markdownTable == """|| Header1 | Header2      |
                            ||---------|--------------|
                            || Cell1   | Longer Cell2 |
                            || Cell3   |              |
                            |""".stripMargin().stripIndent()
    }

    def "test max col sizes for simple table"() {
        when:
        int[] sizes = new MarkdownTableFormatter().findMaxColSizes(TestData.generateSimpleTable())

        then:
        sizes == [7, 12] as int[]
    }
}
