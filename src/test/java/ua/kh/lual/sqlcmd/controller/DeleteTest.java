package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.controller.command.Delete;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DeleteTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Delete();
    }

    @Test
    public void testDelete() {
        when(dbManager.getTableHeader("users")).thenReturn(new LinkedHashSet<String>(Arrays.asList("id", "name", "password")));
        when(dbManager.getFilteredContent(eq("users"), any(LinkedHashMap.class))).thenReturn(new LinkedList<List>(Arrays.asList(
                new ArrayList(Arrays.asList("3", "Marlen", "jasasyn"))
        )));

        // when
        cmd.process("delete|users|name|Marlen");
        // then
        assertOutput( "" +
                "+------+----------+------------+\n" +
                "+  id  +   name   +  password  +\n" +
                "+------+----------+------------+\n" +
                "+  3   +  Marlen  +  jasasyn   +\n" +
                "+------+----------+------------+\n" +
                "Rows above where deleted\n");
        verify(dbManager).delete(eq("users"), any(LinkedHashMap.class));
    }

    @Test
    public void testDeleteNothing() {
        // given
        when(dbManager.getTableHeader("users")).thenReturn(new LinkedHashSet<String>(Arrays.asList("id", "name", "password")));
        when(dbManager.getAllContent("user")).thenReturn(new LinkedList<List>());
        // when
        cmd.process("delete|users|name|Marlen");
        // then
        verify(view).write("Nothing matches key field. No delete performed");
        verify(dbManager, never()).delete(anyString(), any(LinkedHashMap.class));
    }

}
