package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;
import ua.kh.lual.sqlcmd.utils.TextTable;

import java.util.*;

public class Update extends UserCommandClass {
    @Override
    public String format() {
        return "update|tableName|destColumn|destValue|keyColumn|keyValue";
    }

    @Override
    public String description() {
        return "Updates value of specified cells in the table <tableName>" +
                "\tdestColumn - name of column to update\n" +
                "\tdestValue  - new value for column to update\n" +
                "\tkeyColumn  - name of column to check before update\n" +
                "\tkeyValue   - update occurs if keyValue equals actual value of keyColumn\n" +
                "\t  * data in several rows could be updated";
    }

    @Override
    protected void execute(List<String> parameters) {
        try {
            String table = parameters.get(0);
            Map<String, Object> whereRecord= new LinkedHashMap<>();
            whereRecord.put(parameters.get(3), parameters.get(4));
            List<List> updatePreviousState = dbManager.getFilteredContent(table, whereRecord);
            if (updatePreviousState.size() == 0) {
                view.write("Nothing matches key field. No update performed");
                return;
            }
            view.write(new TextTable(dbManager.getTableHeader(table), updatePreviousState, 2).toString());
            Map<String, Object> updateRecord= new LinkedHashMap<>();
            updateRecord.put(parameters.get(1), parameters.get(2));
            dbManager.update(table, updateRecord, whereRecord);
            view.write("Rows above where updated");
        } catch (DBManagerException e) {
            throw new CommandFailedException("JDBCManager error: " + e.getMessage());
        }
    }
}
