package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Exit;
import ua.kh.lual.sqlcmd.controller.exceptions.ExitException;

import static junit.framework.TestCase.fail;

public class ExitTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Exit();
    }

    @Test
    public void testExit() {
        // when
        try {
            cmd.process("exit");
            fail("ExitException was expected but wasn't thrown");
        } catch (ExitException e) {
            // do nothing
        }
        // then
        assertOutput( "" +
                "Bye\n" +
                "See you later ;)\n");
    }

}
