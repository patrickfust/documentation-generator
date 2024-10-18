package dk.fust.docgen

import dk.fust.docgen.format.table.Cell
import dk.fust.docgen.format.table.Row
import dk.fust.docgen.format.table.Table
import dk.fust.docgen.model.DataType

class TestData {

    static Table generateTable() {
        return new Table(
                tableClass: 'myTableClass',
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
                                        new Cell(content: '<h5>Table Name</h5>'),
                                        new Cell(content: '<h5>Column Name</h5>'),
                                        new Cell(content: '<h5>Data Type</h5>'),

                                        new Cell(content: '<h5>Database Name</h5>'),
                                        new Cell(content: '<h5>Table Name</h5>'),
                                        new Cell(content: '<h5>Column Name</h5>'),
                                        new Cell(content: '<h5>Data Type</h5>'),
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
