package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Clear;
import ua.kh.lual.sqlcmd.controller.exceptions.CommandFailedException;
import ua.kh.lual.sqlcmd.model.DBManagerException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ACommonFailsTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Clear();
    }

    @Test
    public void TestCanProcessTrue() {
        assertTrue(cmd.canProcess(cmd.format()));
    }

    @Test
    public void TestCanProcessFalse() {
        assertFalse(cmd.canProcess("+" + cmd.format()));
    }

    @Test
    public void TestProcessNotEnoughParameters() {
        // when
        String message = "";
        try {
            cmd.process("clear");
            fail("CommandFailedException was expected, but wasn't thrown");
        } catch (CommandFailedException e) {
            message = e.getMessage();
        }
        // then
        String expected = "Not enough parameters\n" +
                          "Please use format: <clear|tableName>";
        assertEquals(expected, message);
    }

    @Test
    public void TestProcessTooManyParameters() {
        // when
        String message = "";
        try {
            cmd.process("clear|users|forever");
            fail("CommandFailedException was expected, but wasn't thrown");
        } catch (CommandFailedException e) {
            message = e.getMessage();
        }
        // then
        String expected = "Too many parameters\n" +
                          "Please use format: <clear|tableName>";
        assertEquals(expected, message);
    }

    @Test
    public void TestProcessNotConnected() {
        // given
        when(dbManager.isConnected()).thenReturn(false);
        // when
        String message = "";
        try {
            cmd.process("clear|user");
            fail("CommandFailedException was expected, but wasn't thrown");
        } catch (CommandFailedException e) {
            message = e.getMessage();
        }
        // then
        verify(dbManager).isConnected();
        String expected = "Please connect to database before using command clear|user\n" +
                          "\tUse command <connect|database|user|password";
        assertEquals(expected, message);
    }

    @Test
    public void TestProcessJDBCException() {
        // given
        doThrow(new DBManagerException("JDBCException")).when(dbManager).clearTable(anyString());
        // when
        try {
            cmd.process("clear|user");
            fail("DBManagerException was expected, but wasn't thrown");
        } catch (CommandFailedException e) {
        }
    }
}

