package ua.kh.lual.sqlcmd.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface DatabaseManager {

    boolean isConnected();

    void connect(String database, String user, String password);

    void disconnect();

    Set<String> getTableNames();

    Set<String> getTableHeader(String tableName);

    List<List> getAllContent(String tableName);

    List<List> getFilteredContent(String tableName, Map<String, Object> key);

    void createTable(String tableName, Set<String> columns);

    void dropTable(String tableName);

    void insert(String tableName, Map<String, Object> record);

    void update(String tableName, Map<String, Object> update, Map<String, Object> where);

    void clearTable(String tableName);

    void delete(String tableName, Map<String, Object> key);

}
