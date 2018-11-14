package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Clear;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class ClearTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Clear();
    }

    @Test
    public void testClear() {
        // when
        cmd.process("clear|user");
        // then
        verify(dbManager).clearTable("user");
        verify(view).write("Table <user> was cleared");
    }

}

