package ua.kh.lual.sqlcmd.view;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class Console implements View {

    private static String bold = "\033[1;33m";
    private static String normal = "\033[0;37m";

    public static void suppressFormating() {
        bold = "";
        normal = "";
    }

    @Override
    public void write(String message) {
        String toPrint = normal +
                         message.replaceAll("<", bold).
                                 replaceAll(">", normal).
                                 replaceAll("\n", System.lineSeparator());
        System.out.println(toPrint);
    }

    @Override
    public String read() {
        try {
            Scanner scanner = new Scanner(System.in);
            return scanner.nextLine();
        } catch (NoSuchElementException e) {
            return "";
        }
    }
}

