package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;
import ua.kh.lual.sqlcmd.utils.TextTable;

import java.util.List;

public class Find extends UserCommandClass {

    @Override
    public String format() {
        return "find|tableName";
    }

    @Override
    public String description() {
        return  "Prints content of the table <tableName>";
    }

    @Override
    protected void execute(List<String> parameters) {
        try {
            String table = parameters.get(0);
            view.write(new TextTable(dbManager.getTableHeader(table),
                    dbManager.getAllContent(table),
                    2).toString());
        } catch (DBManagerException e) {
            throw new CommandFailedException("JDBCManager error: " + e.getMessage());
        }
    }
}
