package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Insert;

import java.util.LinkedHashMap;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class InsertTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Insert();
    }

    @Test
    public void testClear() {
        // when
        cmd.process("insert|users|id|5|name|server|password|jamala");
        // then
        verify(dbManager).insert(eq("users"), any(LinkedHashMap.class));
        verify(view).write("New data added successfully");
    }

}
