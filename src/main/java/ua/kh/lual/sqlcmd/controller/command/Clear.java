package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;

import java.util.List;

public class Clear extends UserCommandClass {
    @Override
    public String format() {
        return "clear|tableName";
    }

    @Override
    public String description() {
        return  "Clears table <tableName>";
    }

    @Override
    protected void execute(List<String> parameters) {
        try {
            String table = parameters.get(0);
            dbManager.clearTable(table);
            view.write("Table <" + table + "> was cleared");
        } catch (DBManagerException e) {
            throw new CommandFailedException("JDBCManager error: " + e.getMessage());
        }
    }
}
