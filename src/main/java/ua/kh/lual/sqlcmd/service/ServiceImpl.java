package ua.kh.lual.sqlcmd.service;

import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.model.JDBCManager;
import java.util.*;

public class ServiceImpl implements Service {

    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "tables", "find");
    }

    @Override
    public DatabaseManager connect(String dbName, String userName, String password) {
        DatabaseManager dbManager = new JDBCManager();
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
}
