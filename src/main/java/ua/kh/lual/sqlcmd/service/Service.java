package ua.kh.lual.sqlcmd.service;

import ua.kh.lual.sqlcmd.model.DatabaseManager;

import java.util.List;

public interface Service {
    List<String> commandsList();
    DatabaseManager connect(String dbName, String userName, String password);
    List<List<String>> find(DatabaseManager dbManager, String tableName);
}
