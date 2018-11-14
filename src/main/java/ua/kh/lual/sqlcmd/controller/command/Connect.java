package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;

import java.util.List;

public class Connect extends UserCommandClass {
    @Override
    public String format() {
        return "connect|database|user|password";
    }

    @Override
    public String description() {
        return "Connects to database <database> as user <user> with password <password>";
    }

    @Override
    protected void execute(List<String> parameters) {
        try {
            String database = parameters.get(0);
            String user = parameters.get(1);
            String password = parameters.get(2);
            dbManager.connect(database, user, password);
            view.write(String.format("User <%s> successfully connected to database <%s>", user, database));
        } catch (DBManagerException e) {
            throw new CommandFailedException("JDBCManager error: " + e.getMessage());
        }
    }

    @Override
    public boolean requestsConnection() {
        return false;
    }

}
