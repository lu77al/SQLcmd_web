package ua.kh.lual.sqlcmd.controller.exceptions;

public class CommandFailedException  extends RuntimeException {
    public CommandFailedException(String s) {
        super(s);
    }
}
