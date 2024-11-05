package dk.fust.docgen.format.table

class MockTableFormatter implements TableFormatter {

    String mockFormat
    FormatTable formatTableArgument

    @Override
    String formatTable(FormatTable formatTable) {
        this.formatTableArgument = formatTable
        return mockFormat
    }

}
