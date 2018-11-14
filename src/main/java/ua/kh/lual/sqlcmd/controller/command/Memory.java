package ua.kh.lual.sqlcmd.controller.command;

import java.util.List;

public class Memory extends UserCommandClass {
    @Override
    public String format() {
        return "memory";
    }

    @Override
    public String description() {
        return "For switching to memory database manager realization";
    }

    @Override
    protected void execute(List<String> parameters) {
        UserCommandClass.dbManager = memoryDBManager;
        view.write("You have switched to memory database manager realization");
    }

    @Override
    public boolean requestsConnection() {
        return false;
    }

}
