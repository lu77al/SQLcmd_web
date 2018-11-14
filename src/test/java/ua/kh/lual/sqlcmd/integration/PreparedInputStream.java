package ua.kh.lual.sqlcmd.integration;

import java.io.IOException;
import java.io.InputStream;

public class PreparedInputStream extends InputStream {
    private String userInput = "";
    private StringBuilder appOutput = new StringBuilder();

    @Override
    public int read() throws IOException {
        int result;
        if (userInput.length() == 0) {
            result = -1;
        } else {
            char ch = userInput.charAt(0);
            userInput = userInput.substring(1);
            result = (short)ch;
        }
        if (result == -1) {
            System.out.println(appOutput.toString());
            appOutput.setLength(0);
        } else if (result != '\n') {
            appOutput.append((char)result);
        }
        return result;
    }

    public void userTypes(String line) {
        userInput += line + "\n\uffff";
    }

    public void clear() {
        userInput = "";
        appOutput.setLength(0);
    }

}
