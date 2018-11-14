package ua.kh.lual.sqlcmd.controller;

import ua.kh.lual.sqlcmd.controller.command.*;
import ua.kh.lual.sqlcmd.controller.command.Main;
import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.controller.exceptions.ExitException;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.view.View;

import java.util.LinkedList;
import java.util.List;

public class Controller {

    private View view;

    private List<UserCommand> commands = new LinkedList<>();

    public Controller(View view, DatabaseManager dbManager) {
        this.view = view;

        UserCommandClass.setView(view);
        UserCommandClass.setDbManager(dbManager);

        Help help = new Help();

        commands.add(new Connect());
        commands.add(new Memory());
        commands.add(new Main());
        commands.add(new Tables());
        commands.add(new Find());
        commands.add(new Clear());
        commands.add(new Insert());
        commands.add(new Update());
        commands.add(new Delete());
        commands.add(new Create());
        commands.add(new Drop());
        commands.add(new Exit());
        commands.add(new Test());
        commands.add(help);

        help.setCommandList(commands);
    }

    public void run() {
        view.write("Hello. Your are using SQLcmd application");
        try {
            mainLoop();
        } catch (ExitException e) {
            // Do nothing
        }
    }

    private void mainLoop() {
        while (true) {
            view.write("");
            view.write("Enter command (<help> for commands list)");
            String userInput = view.read();
            if (userInput.length() > 0) {
                executeUserCommand(userInput);
            }
        }
    }

    private void executeUserCommand(String userInput) {
        for (UserCommand command : commands) {
            if (command.canProcess(userInput)) {
                try {
                    command.process(userInput);
                } catch (CommandFailedException e) {
                    view.write("Command failed");
                    view.write(e.getMessage());
                }
                return;
            }
        }
        view.write("Unknown command: " + userInput);
    }

}