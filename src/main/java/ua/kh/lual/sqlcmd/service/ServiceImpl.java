package ua.kh.lual.sqlcmd.service;

import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.model.JDBCManager;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {
    private DatabaseManager dbManager;

    public ServiceImpl() {
        dbManager = new JDBCManager();
    }

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "tables");
    }

    @Override
    public void connect(String dbName, String userName, String password) {
        dbManager.connect(dbName, userName, password);
    }
}
