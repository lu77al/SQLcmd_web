package ua.kh.lual.sqlcmd.service;

import ua.kh.lual.sqlcmd.model.DatabaseManager;

public abstract class ConnectionService {

    public abstract DatabaseManager getManager();

    public DatabaseManager connect(String dbName, String userName, String password) {
        DatabaseManager dbManager;
        dbManager = getManager();
        dbManager.connect(dbName, userName, password);
        return dbManager;
    }
}
