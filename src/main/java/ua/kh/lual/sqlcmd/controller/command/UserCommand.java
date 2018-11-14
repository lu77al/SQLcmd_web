package ua.kh.lual.sqlcmd.controller.command;

import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.view.View;

public interface UserCommand {

    boolean canProcess(String command);

    void process(String command) throws CommandFailedException;

    String format();

    String description();

}
