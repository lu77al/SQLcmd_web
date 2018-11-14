package ua.kh.lual.sqlcmd.controller;

import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.model.JDBCManager;
import ua.kh.lual.sqlcmd.view.Console;
import ua.kh.lual.sqlcmd.view.View;

public class Main {
    public static void main(String[] args) {
        View view = new Console();
        DatabaseManager manager = new JDBCManager();
        Controller controller = new Controller(view, manager);
        controller.run();
    }
}
