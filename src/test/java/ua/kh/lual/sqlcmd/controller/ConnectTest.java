package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Clear;
import ua.kh.lual.sqlcmd.controller.command.Connect;

import static org.mockito.Mockito.verify;

public class ConnectTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Connect();
    }

    @Test
    public void testConnect() {
        // when
        cmd.process("connect|sqlcmd|postgres|12345");
        // then
        verify(dbManager).connect("sqlcmd", "postgres", "12345");
        verify(view).write("User <postgres> successfully connected to database <sqlcmd>");
    }

}

