package dk.fust.docgen.format.table;

/**
 * Converts from model of a Table to a HTML representation
 */
public class HTMLTableFormatter implements TableFormatter {

    @Override
    public String formatTable(FormatTable formatTable) {
        StringBuilder sb = new StringBuilder();
        if (formatTable.getTableClass() != null && !formatTable.getTableClass().isEmpty()) {
            sb.append("<table class=\"%s\">".formatted(formatTable.getTableClass()));
        } else {
            sb.append("<table>");
        }
        sb.append(generateColGroup(formatTable));
        sb.append(generateBody(formatTable));
        sb.append("</table>");
        return sb.toString();
    }

    private String generateBody(FormatTable formatTable) {
        if (formatTable.getRows() != null && !formatTable.getRows().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n<tbody>\n");
            for (Row row : formatTable.getRows()) {
                sb.append("<tr>");
                for (Cell cell : row.getCells()) {
                    sb.append(generateCell(cell));
                }
                sb.append("</tr>\n");
            }
            sb.append("</tbody>\n");
            return sb.toString();
        } else {
            return "";
        }
    }

    private String generateCell(Cell cell) {
        StringBuilder sb = new StringBuilder();
        String tag = cell.isHeader() ? "th" : "td";
        sb.append("<").append(tag);
        if (cell.getColspan() > 1) {
            sb.append(" colspan=\"").append(cell.getColspan()).append("\"");
        }
        sb.append(">");
        sb.append(cell.getContent());
        sb.append("</%s>".formatted(tag));
        return sb.toString();
    }

    private String generateColGroup(FormatTable formatTable) {
        if (formatTable.getColGroup() != null &&
                formatTable.getColGroup().getCols() != null && !formatTable.getColGroup().getCols().isEmpty()) {
            StringBuilder sb = new StringBuilder();
            sb.append("<colgroup>");
            for (Col col : formatTable.getColGroup().getCols()) {
                if (col.getColspan() == 1 && (col.getBackgroundColor() == null || col.getBackgroundColor().isEmpty())) {
                    sb.append("<col/>");
                } else {
                    sb.append("<col");
                    if (col.getBackgroundColor() != null && !col.getBackgroundColor().isEmpty()) {
                        sb.append(" background-color=\"").append(col.getBackgroundColor()).append("\"");
                    }
                    if (col.getColspan() != 1) {
                        sb.append(" colspan=\"").append(col.getColspan()).append("\"");
                    }
                    sb.append("/>");
                }
            }
            sb.append("</colgroup>");
            return sb.toString();
        } else {
            return "";
        }
    }

}
