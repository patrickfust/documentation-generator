package dk.fust.docgen

import dk.fust.docgen.format.table.Cell
import dk.fust.docgen.format.table.Col
import dk.fust.docgen.format.table.ColGroup
import dk.fust.docgen.format.table.Row
import dk.fust.docgen.format.table.FormatTable
import dk.fust.docgen.model.DataType

class TestData {

    static FormatTable generateTable() {
        return new FormatTable(
                tableClass: 'myTableClass',
                colGroup: new ColGroup(cols: [new Col(), new Col()]),
                rows: [
                        new Row(
                                cells: [
                                        new Cell(
                                                colspan: 3,
                                                content: 'With colspan 3',
                                                header: true
                                        ),
                                        new Cell(
                                                colspan: 4,
                                                content: 'With colspan 4',
                                                header: true
                                        ),
                                        new Cell(
                                                content: 'No colspan',
                                                header: true
                                        )
                                ]
                        ),
                        new Row(
                                cells: [
                                        new Cell(content: 'Table Name'),
                                        new Cell(content: 'Column Name'),
                                        new Cell(content: 'Data Type'),

                                        new Cell(content: 'Database Name'),
                                        new Cell(content: 'Table Name'),
                                        new Cell(content: 'Column Name'),
                                        new Cell(content: 'Data Type'),
                                ]
                        ),
                        new Row(
                                cells: [
                                        new Cell(content: 'table_a'),
                                        new Cell(content: 'column_a'),
                                        new Cell(content: DataType.INT.toLowerCase()),
                                        new Cell(content: 'database name'),
                                        new Cell(content: 'table_b'),
                                        new Cell(content: 'column_b'),
                                        new Cell(content: DataType.TEXT.toLowerCase()),
                                        new Cell(content: 'some transformation')
                                ]
                        )
                ]
        )
    }
}
