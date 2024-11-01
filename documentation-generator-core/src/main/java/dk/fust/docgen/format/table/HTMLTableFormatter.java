package dk.fust.docgen.format.table;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * Converts from model of a Table to a HTML representation
 */
@Slf4j
@Data
public class HTMLTableFormatter implements TableFormatter {

    private Map<String, String> dataFields;
    private List<String> columnWidths;

    @Override
    public String formatTable(FormatTable formatTable) {
        StringBuilder sb = new StringBuilder();
        sb.append("<table");

        if (formatTable.getTableClass() != null && !formatTable.getTableClass().isEmpty()) {
            sb.append(" class=\"%s\"".formatted(formatTable.getTableClass()));
        }
        if (dataFields != null && !dataFields.isEmpty()) {
            dataFields.forEach((key, value) -> {
                sb.append(" data-%s=\"%s\"".formatted(key, value));
            });
        }
        sb.append(">");
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
            int colIdx = 0;
            for (Col col : formatTable.getColGroup().getCols()) {
                if (col.getColspan() == 1 && (col.getBackgroundColor() == null || col.getBackgroundColor().isEmpty())
                        || columnWidths == null || columnWidths.isEmpty()) {
                    sb.append("<col/>");
                } else {
                    sb.append("<col");
                    if (col.getBackgroundColor() != null && !col.getBackgroundColor().isEmpty()) {
                        sb.append(" background-color=\"").append(col.getBackgroundColor()).append("\"");
                    }
                    if (col.getColspan() != 1) {
                        sb.append(" colspan=\"").append(col.getColspan()).append("\"");
                    }
                    if (columnWidths.size() > colIdx) {
                        sb.append(" style=\"width: %spx;\"".formatted(columnWidths.get(colIdx)));
                    }
                    sb.append("/>");
                }
                colIdx++;
            }
            sb.append("</colgroup>");
            return sb.toString();
        } else {
            return "";
        }
    }

}
