package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;
import ua.kh.lual.sqlcmd.utils.TextTable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Delete extends UserCommandClass{
    @Override
    public String format() {
        return "delete|tableName|column|value";
    }

    @Override
    public String description() {
        return "Deletes rows from table <tableName> in which column <column> has value <value>";
    }

    @Override
    protected void execute(List<String> parameters) {
        try {
            String table = parameters.get(0);
            Map<String, Object> whereRecord = new LinkedHashMap<>();
            whereRecord.put(parameters.get(1), parameters.get(2));
            List<List> updatePreviousState = dbManager.getFilteredContent(table, whereRecord);
            if (updatePreviousState.size() == 0) {
                view.write("Nothing matches key field. No delete performed");
                return;
            }
            view.write(new TextTable(dbManager.getTableHeader(table), updatePreviousState, 2).toString());
            dbManager.delete(table, whereRecord);
            view.write("Rows above where deleted");
        } catch (DBManagerException e) {
            throw new CommandFailedException("JDBCManager error: " + e.getMessage());
        }

    }
}
