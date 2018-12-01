package ua.kh.lual.sqlcmd.service;

import org.springframework.stereotype.Component;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.model.JDBCManager;

@Component
public class DBManagerFactory {

    public String dbClass;

    public void setDbClass(String dbClass) {
        this.dbClass = dbClass;
    }

    public DatabaseManager getNewDBManager() {
        try {
            return (DatabaseManager) Class.forName(dbClass).newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}
