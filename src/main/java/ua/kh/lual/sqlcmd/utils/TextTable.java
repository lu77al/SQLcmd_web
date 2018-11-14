package ua.kh.lual.sqlcmd.utils;

import java.util.*;

public class TextTable {
    private Set header;
    private List<List> content;
    private int margin;


    public TextTable(Set header, List<List> content, int margin) {
        this.header = header;
        this.content = content;
        this.margin = margin;
    }

    @Override
    public String toString() {
        List<Integer> columnsWidth = getColumnsWidth();
        String horizontalLine = getTableHorizontalLine(columnsWidth);
        String result = horizontalLine + '\n';
        result += getTableRow(new ArrayList(header), columnsWidth) + '\n';
        if (content.size() != 0) {
            result += horizontalLine + '\n';
            for (List row : content) {
                result += getTableRow(row, columnsWidth) + '\n';
            }
        }
        result += horizontalLine;
        return result;
    }

    private static final String nullString = "[null]";

    private List<Integer> getColumnsWidth() {
        List<Integer> columnWidth = new ArrayList<>(header.size());
        int columnIndex = 0;
        for (Object column: header) {
            int biggestWidth = column.toString().length();
            for (List row: content) {
                Object cell = row.get(columnIndex);
                int width;
                if (cell != null) {
                    width = cell.toString().length();
                } else {
                    width = nullString.length();
                }
                if (width > biggestWidth) {
                    biggestWidth = width;
                }
            }
            columnWidth.add(columnIndex, biggestWidth +  margin * 2);
            columnIndex++;
        }
        return columnWidth;
    }

    private String getTableHorizontalLine(List<Integer> columnWidth) {
        StringBuilder result = new StringBuilder("+");
        for (Integer width: columnWidth) {
            for (int i = 0; i < width; i++) {
                result.append('-');
            }
            result.append('+');
        }
        return result.toString();
    }

    private String getTableRow(List items, List columnWidth) {
        StringBuilder result = new StringBuilder("+");
        Iterator<Integer> widthIterator = columnWidth.iterator();
        for (Object cellObject: items) {
            String cell = (cellObject != null) ? cellObject.toString() : nullString;
            int width = widthIterator.next();
            int frontMargin = (width - cell.length()) / 2;
            for (int j = 0; j < frontMargin; j++) {
                result.append(' ');
            }
            result.append(cell);
            for (int j = 0; j < width - cell.length() - frontMargin; j++) {
                result.append(' ');
            }
            result.append('+');
        }
        return result.toString();
    }

}
