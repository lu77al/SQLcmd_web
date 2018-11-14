package ua.kh.lual.sqlcmd.model;

import java.util.*;

public class MemoryTable {
    private Map<String, Object> headerRow;
    private List<Map<String, Object>> content;

    public MemoryTable(Set<String> header) {
        headerRow = new LinkedHashMap<>();
        header.forEach(name -> headerRow.put(name, null));
        content = new LinkedList<>();
    }

    public Set<String> getHeader() {
        return new LinkedHashSet<>(headerRow.keySet());
    }

    public List<List> getContent() {
        List<List> result = new LinkedList<>();
        content.forEach(row -> result.add(new ArrayList<Object>(row.values())));
        return result;
    }

    public List<List> getFilteredContent(Map<String, Object> key) {
        List<List> result = new LinkedList<>();
        content.forEach(row -> {
            if (row.entrySet().containsAll(key.entrySet())) {
                result.add(new ArrayList<Object>(row.values()));
            }
        });
        return result;
    }

    public void insert(Map<String, Object> record) {
        Map<String, Object> row = new LinkedHashMap<>(headerRow);
        row.putAll(record);
        content.add(row);
    }

    public void update(Map<String, Object> update, Map<String, Object> where) {
        content.forEach(row -> {
            if (row.entrySet().containsAll(where.entrySet())) {
                row.putAll(update);
            }
        });
    }

    public void delete(Map<String, Object> key) {
        ArrayList<Integer> delete = new ArrayList<>();
        int index = 0;
        for (Map<String,Object> row: content) {
            if (row.entrySet().containsAll(key.entrySet())) {
                delete.add(0, index);
            }
            index++;
        }
        delete.forEach(deleteIndex -> content.remove((int)deleteIndex));
    }

    public void clear() {
        content = new LinkedList<>();
    }

}
