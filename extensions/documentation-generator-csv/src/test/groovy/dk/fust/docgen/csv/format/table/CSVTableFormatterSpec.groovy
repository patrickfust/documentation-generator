package dk.fust.docgen.csv.format.table

import dk.fust.docgen.format.table.Cell
import dk.fust.docgen.format.table.FormatTable
import dk.fust.docgen.format.table.Row
import spock.lang.Specification

class CSVTableFormatterSpec extends Specification {

    void "standard CSV"() {
        when:
        CSVTableFormatter csvTableFormatter = new CSVTableFormatter()
        FormatTable formatTable = new FormatTable(rows: [
                new Row(cells: [new Cell("header 1"), new Cell("header 2")]),
                new Row(cells: [new Cell("content 1"), new Cell("content 2")]),
                new Row(cells: [new Cell("content with ' and æøå"), new Cell("content ; \"yep\" &%€")]),
        ])
        String csv = csvTableFormatter.formatTable(formatTable)
        List<String> lines = csv.readLines()

        then:
        lines[0] == "header 1;header 2"
        lines[1] == "content 1;content 2"
        lines[2] == "content with ' and æøå;\"content ; \"\"yep\"\" &%€\""
    }

}
