package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Connect;
import ua.kh.lual.sqlcmd.controller.command.Drop;

import static org.mockito.Mockito.verify;

public class DropTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Drop();
    }

    @Test
    public void testDrop() {
        // when
        cmd.process("drop|users");
        // then
        verify(dbManager).dropTable("users");
        verify(view).write("Table <users> was deleted");
    }

}

