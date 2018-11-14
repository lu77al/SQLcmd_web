package ua.kh.lual.sqlcmd.controller.command;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Help extends UserCommandClass {

    List<UserCommand> commandList;

    @Override
    public String format() {
        return "help";
    }

    @Override
    public String description() {
        return "Prints this brief commands summary";
    }

    public void setCommandList(List<UserCommand> commandList) {
        this.commandList = commandList;
    }

    @Override
    protected void execute(List<String> parameters) {
        view.write("You can use next commands:");
        for (UserCommand cmd: commandList) {
            view.write("\t" + cmd.format());
            List<String> descriptions = new LinkedList<>(Arrays.asList(cmd.description().split("\n")));
            for (String description: descriptions) {
                view.write("\t\t" + description);
            }
        }
    }

    @Override
    public boolean requestsConnection() {
        return false;
    }

}
