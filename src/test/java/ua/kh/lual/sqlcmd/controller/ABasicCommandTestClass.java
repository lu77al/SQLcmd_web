package ua.kh.lual.sqlcmd.controller;

import org.mockito.ArgumentCaptor;
import ua.kh.lual.sqlcmd.controller.command.UserCommand;
import ua.kh.lual.sqlcmd.controller.command.UserCommandClass;
import ua.kh.lual.sqlcmd.model.DatabaseManager;
import ua.kh.lual.sqlcmd.view.View;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.atLeastOnce;

public class ABasicCommandTestClass {

    DatabaseManager dbManager;
    View view;
    UserCommand cmd;

    public void setupMocks() {
        dbManager = mock(DatabaseManager.class);
        view = mock(View.class);
        UserCommandClass.setDbManager(dbManager);
        UserCommandClass.setView(view);
        when(dbManager.isConnected()).thenReturn(true);
    }

    public String getActualOutput() {
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(view, atLeastOnce()).write(captor.capture());
        String result = "";
        for (final String line: captor.getAllValues()) {
            result += line + "\n";
        }
        return result;
    }

    public void assertOutput(String expected) {
        String actual = getActualOutput();
        assertEquals(expected, actual);
    }

}
