package dk.fust.docgen.format.table;

import dk.fust.docgen.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Converts from model of a Table to the way Markdown writes tables
 */
public class MarkdownTableFormatter implements TableFormatter {

    /**
     * Convert to Markdown
     * @param formatTable table to convert
     * @return Markdown table
     */
    @Override
    public String formatTable(FormatTable formatTable) {
        replaceLFWithBreak(formatTable);
        StringBuilder stringBuilder = new StringBuilder(512);
        int[] sizes = findMaxColSizes(formatTable);
        if (formatTable.getRows() != null) {
            for (Row row : formatTable.getRows()) {
                if (row.getCells() != null) {
                    boolean header = false;
                    stringBuilder.append("|");
                    int sizeIdx = 0;
                    for (int cellIdx = 0; cellIdx < row.getCells().size(); cellIdx++) {
                        Cell cell = row.getCells().get(cellIdx);
                        stringBuilder.append(generateCell(cell, sizeIdx, sizes));
                        sizeIdx += Math.max(cell.getColspan(), 1);
                        header = header || cell.isHeader();
                    }
                    stringBuilder.append("\n");
                    if (header) {
                        stringBuilder.append(createHeaderSubLine(sizes, formatTable));
                    }
                }
            }
        }
        return stringBuilder.toString();
    }

    private static String createHeaderSubLine(int[] sizes, FormatTable formatTable) {
        StringBuilder sb = new StringBuilder();
        Row firstRow = formatTable.getRows().get(0);
        List<Cell> cellsExpanded = expandCellsWithColspan(firstRow.getCells());
        for (int idx = 0 ; idx < sizes.length; idx++) {
            int size = sizes[idx];
            Cell cell = cellsExpanded.get(idx);
            sb.append("|");
            switch (cell.getAlignment()) {
                case LEFT -> sb.append("-".repeat(size + 2));
                case RIGHT -> sb.append("-".repeat(size + 1)).append(":");
                case CENTER -> sb.append(":").append("-".repeat(size)).append(":");
            }
        }
        sb.append("|\n");
        return sb.toString();
    }

    private static List<Cell> expandCellsWithColspan(List<Cell> cells) {
        List<Cell> cellsExpanded = new ArrayList<>();
        for (Cell cell : cells) {
            cellsExpanded.add(cell);
            for (int idx = 1; idx < cell.getColspan(); idx++) {
                cellsExpanded.add(cell);
            }
        }
        return cellsExpanded;
    }

    private void replaceLFWithBreak(FormatTable formatTable) {
        if (formatTable.getRows() != null) {
            for (Row row : formatTable.getRows()) {
                if (row.getCells() != null) {
                    for (Cell cell : row.getCells()) {
                        if (cell.getContent() != null) {
                            cell.setContent(cell.getContent().replace("\n", "<br>"));
                        }
                    }
                }
            }
        }
    }

    /**
     * Finding the max size of each column
     * @param formatTable table to find max sizes for
     * @return array of max sizes for each column
     */
    protected int[] findMaxColSizes(FormatTable formatTable) {
        if (formatTable.getRows() != null && !formatTable.getRows().isEmpty()) {
            int cols = findMaxCellSize(formatTable);
            int[] sizes = new int[cols];
            int rows = formatTable.getRows().size();
            for (int rowIdx = 0 ; rowIdx < rows ; rowIdx++) {
                List<Cell> cells = formatTable.getRows().get(rowIdx).getCells();
                int cellIdx = 0;
                for (Cell cell : cells) {
                    sizes[cellIdx] = Math.max(sizes[cellIdx], cell.getContentSize());
                    cellIdx += Math.max(cell.getColspan(), 1);
                }
            }
            return sizes;
        } else {
            return new int[0];
        }
    }

    private int findMaxCellSize(FormatTable formatTable) {
        int maxCellSize = 0;
        for (Row row : formatTable.getRows()) {
            int rowCells = 0;
            for (Cell cell : row.getCells()) {
                rowCells += 1 + Math.max(cell.getColspan() - 1, 0);
                if (rowCells > maxCellSize) {
                    maxCellSize = rowCells;
                }
            }
        }
        return maxCellSize;
    }

    private String generateCell(Cell cell, int cellIdx, int[] sizes) {
        Assert.isTrue(cell.getColspan() > 0, "Colspan must be greater than 0");
        StringBuilder stringBuilder = new StringBuilder(sizes[cellIdx] + 4);
        stringBuilder.append(" ");
        stringBuilder.append(rightpad(cell.getContent(), sizes[cellIdx]));
        for (int colSpanIdx = 0 ; colSpanIdx < cell.getColspan() - 1 ; colSpanIdx++) {
            stringBuilder.append(" | ");
            stringBuilder.append(" ".repeat(sizes[cellIdx + colSpanIdx + 1]));
        }
        stringBuilder.append(" |");
        return stringBuilder.toString();
    }

    private String rightpad(String text, int length) {
        if (text == null) {
            text = "";
        }
        return String.format("%-" + length + "." + length + "s", text);
    }

}
