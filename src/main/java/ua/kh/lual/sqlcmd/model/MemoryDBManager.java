package ua.kh.lual.sqlcmd.model;

import java.util.*;

public class MemoryDBManager implements DatabaseManager {
    private boolean connected = false;
    private LinkedHashMap<String, MemoryTable> tables = new LinkedHashMap<>();

    @Override
    public boolean isConnected() {
        return connected;
    }

    @Override
    public Set<String> getTableNames() {
        return tables.keySet();
    }

    @Override
    public void connect(String database, String user, String password) {
        connected = true;
    }

    @Override
    public void disconnect() {
        connected = false;
    }

    private MemoryTable getTableByName(String tableName) {
        MemoryTable result = tables.get(tableName);
        if (result == null) {
            throw new DBManagerException("Can't find table " + tableName);
        }
        return result;
    }

    @Override
    public Set<String> getTableHeader(String tableName) {
        MemoryTable table = getTableByName(tableName);
        return table.getHeader();
    }

    @Override
    public List<List> getAllContent(String tableName) {
        MemoryTable table = getTableByName(tableName);
        return table.getContent();
    }

    @Override
    public List<List> getFilteredContent(String tableName, Map<String, Object> key) {
        MemoryTable table = getTableByName(tableName);
        return table.getFilteredContent(key);
    }

    @Override
    public void clearTable(String tableName) {
        MemoryTable table = getTableByName(tableName);
        table.clear();
    }

    @Override
    public void insert(String tableName, Map<String, Object> record) {
        MemoryTable table = getTableByName(tableName);
        table.insert(record);
    }

    @Override
    public void update(String tableName, Map<String, Object> update, Map<String, Object> where) {
        MemoryTable table = getTableByName(tableName);
        table.update(update, where);
    }

    @Override
    public void delete(String tableName, Map<String, Object> key) {
        MemoryTable table = getTableByName(tableName);
        table.delete(key);
    }

    @Override
    public void createTable(String tableName, Set<String> columns) {
        if (tables.get(tableName) != null) {
            throw new DBManagerException("Can't create table " + tableName);
        }
        tables.put(tableName, new MemoryTable(columns));
    }

    @Override
    public void dropTable(String tableName) {
        getTableByName(tableName);
        tables.remove(tableName);
    }
}
