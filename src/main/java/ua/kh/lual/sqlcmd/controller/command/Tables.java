package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;

import java.util.List;

public class Tables extends UserCommandClass {

    @Override
    public String format() {
        return "tables";
    }

    @Override
    public String description() {
        return "Prints tables names of connected database";
    }

    @Override
    protected void execute(List<String> parameters) {
        try {
            view.write(dbManager.getTableNames().toString());
        } catch (DBManagerException e) {
            throw new CommandFailedException("JDBCManager error: " + e.getMessage());
        }
    }
}
