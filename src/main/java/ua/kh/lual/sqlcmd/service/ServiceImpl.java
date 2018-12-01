package ua.kh.lual.sqlcmd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.model.JDBCManager;
import java.util.*;

@Component(value = "postgresService")
public class ServiceImpl implements Service {

    @Autowired
    private DBManagerFactory dbManagerFactory;

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "tables", "find");
    }

    @Override
    public DatabaseManager connect(String dbName, String userName, String password) {
        DatabaseManager dbManager = dbManagerFactory.getNewDBManager();
        dbManager.connect(dbName, userName, password);
        return dbManager;
    }

    @Override
    public List<List<String>> find(DatabaseManager dbManager, String tableName) {
        List<List<String>> result = new LinkedList<>();
        List<String> header = new ArrayList<>(dbManager.getTableHeader(tableName));
        result.add(header);
        List<List> content = dbManager.getAllContent(tableName);
        for (List row: content) {
            List<String> nextRow = new ArrayList<>(header.size());
            for (Object cell: row) {
                nextRow.add(cell.toString());
            }
            result.add(nextRow);
        }
        return result;
    }

    @Override
    public Set<String> tables(DatabaseManager dbManager) {
        return dbManager.getTableNames();
    }

    @Override
    public void create(DatabaseManager dbManager, String tableName, Set<String> newHeader) {
        dbManager.createTable(tableName, newHeader);
    }

    @Override
    public void drop(DatabaseManager dbManager, String tableName) {
        dbManager.dropTable(tableName);
    }

}
