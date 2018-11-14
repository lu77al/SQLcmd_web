package ua.kh.lual.sqlcmd.controller.command;

import java.util.List;

public class Main extends UserCommandClass {
    @Override
    public String format() {
        return "main";
    }

    @Override
    public String description() {
        return "For switching to main (usually postgress) database manager realization";
    }

    @Override
    protected void execute(List<String> parameters) {
        UserCommandClass.dbManager = mainDBManager;
        view.write("You have switched to main database manager realization");
    }

    @Override
    public boolean requestsConnection() {
        return false;
    }

}
