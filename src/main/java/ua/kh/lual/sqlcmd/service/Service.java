package ua.kh.lual.sqlcmd.service;

import ua.kh.lual.sqlcmd.model.DatabaseManager;

import java.util.List;
import java.util.Set;

public interface Service {
    List<String> commandsList();
    DatabaseManager connect(String dbName, String userName, String password);
    List<List<String>> find(DatabaseManager dbManager, String tableName);
    Set<String> tables(DatabaseManager dbManager);
    void create(DatabaseManager dbManager, String tableName, Set<String> newHeader);
    void drop(DatabaseManager dbManager, String tableName);
}
