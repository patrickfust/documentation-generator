package dk.fust.docgen.format.table

class MockTableFormatter implements TableFormatter {

    String formattet
    FormatTable formatTableArgument

    @Override
    String formatTable(FormatTable formatTable) {
        this.formatTableArgument = formatTable
        return formattet
    }

}
