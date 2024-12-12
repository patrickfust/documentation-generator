package dk.fust.docgen.format.table

import spock.lang.Specification

class JsonTableFormatterSpec extends Specification {

    def "generate simple table"() {
        when:
        String jsonTable = new JsonTableFormatter().formatTable(makeTable())

        then:
        jsonTable == """[ {
        |  "key1" : "value1_1",
        |  "key2" : "value1_2"
        |}, {
        |  "key1" : "value2_1",
        |  "key2" : "value2_2"
        |} ]""".stripMargin().stripIndent()
    }

    def "generate simple table with no pretty print"() {
        when:
        JsonTableFormatter formatter = new JsonTableFormatter(prettyPrint: false)
        String jsonTable = formatter.formatTable(makeTable())

        then:
        jsonTable == '[{"key1":"value1_1","key2":"value1_2"},{"key1":"value2_1","key2":"value2_2"}]'
    }

    def "generate simple table as yaml"() {
        when:
        String jsonTable = new JsonTableFormatter(yaml: true).formatTable(makeTable())

        then:
        jsonTable == """ \
        |- key1: "value1_1"
        |  key2: "value1_2"
        |- key1: "value2_1"
        |  key2: "value2_2"
        |""".stripMargin().stripIndent()
    }

    private static FormatTable makeTable() {
        return new FormatTable(
                rows: [new Row(cells: [
                        new Cell("key1"),
                        new Cell("key2"),
                ]),
                       new Row(cells: [
                               new Cell("value1_1"),
                               new Cell("value1_2"),
                       ]),
                       new Row(cells: [
                               new Cell("value2_1"),
                               new Cell("value2_2"),
                       ])
                ]
        )
    }

}
