package dk.fust.docgen.format.table;

import dk.fust.docgen.util.Assert;

/**
 * Converts from model of a Table to the way Markdown writes tables
 */
public class MarkdownTableFormat implements TableFormat {

    /**
     * Convert to Markdown
     * @param table table to convert
     * @return markdown table
     */
    @Override
    public String generateTable(Table table) {
        StringBuilder stringBuilder = new StringBuilder(512);
        for (Row row : table.getRows()) {
            boolean header = false;
            for (Cell cell : row.getCells()) {
                stringBuilder.append(generateCell(cell));
                header = header || cell.isHeader();
            }
            stringBuilder.append(" |\n");
            if (header) {
                for (Cell cell : row.getCells()) {
                    stringBuilder.append(" | --".repeat(cell.getColspan()));
                }
                stringBuilder.append(" |\n");
            }
        }

        return stringBuilder.toString();
    }

    private String generateCell(Cell cell) {
        Assert.isTrue(cell.getColspan() > 0, "Colspan must be greater than 0");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" | ");
        stringBuilder.append(cell.getContent());
        stringBuilder.append(" | ".repeat(cell.getColspan() - 1));
        return stringBuilder.toString();
    }

}
