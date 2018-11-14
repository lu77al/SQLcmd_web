package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.model.MemoryDBManager;
import ua.kh.lual.sqlcmd.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class UserCommandClass implements UserCommand{
    static View view;
    static DatabaseManager dbManager;
    static DatabaseManager mainDBManager;
    static DatabaseManager memoryDBManager = new MemoryDBManager();

    public static void setView(View view) {
        UserCommandClass.view = view;
    }

    public static void setDbManager(DatabaseManager dbManager) {
        if (!memoryDBManager.equals(UserCommandClass.dbManager)) {
            UserCommandClass.dbManager = dbManager;
        }
        UserCommandClass.mainDBManager = dbManager;
    }

    @Override
    public abstract String format();

    @Override
    public abstract String description();

    @Override
    public boolean canProcess(String command) {
        List<String> estimated = new ArrayList<>(Arrays.asList(format().split("\\|")));
        List<String> entered = new ArrayList<>(Arrays.asList(command.split("\\|")));
        if (entered.size() == 0) {
            return false;
        }
        return estimated.get(0).equals(entered.get(0));
    }

    @Override
    public void process(String command) {
        if (requestsConnection()) {
            throw new CommandFailedException("Please connect to database before using command " + command +
                    "\n\tUse command <" + new Connect().format());
        }
        List<String> chunks = new ArrayList<>(Arrays.asList(command.split("\\|")));
        checkParametersCount(chunks.size() - 1);
        execute(chunks.subList(1, chunks.size()));
    }

    protected abstract void execute(List<String> parameters);

    protected void checkParametersCount(int actualCount) {
        int expectedCount = format().split("\\|").length - 1;
        if (expectedCount == actualCount) return;
        String errorMessage;
        if (actualCount < expectedCount) {
            errorMessage = "Not enough";
        } else {
            errorMessage = "Too many";
        }
        errorMessage += " parameters\nPlease use format: <" + format() + ">";
        throw new CommandFailedException(errorMessage);
    }

    protected boolean requestsConnection() {
        return !dbManager.isConnected();
    }
}
