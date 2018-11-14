package ua.kh.lual.sqlcmd.controller;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;
import ua.kh.lual.sqlcmd.controller.command.Find;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class FindTest extends ABasicCommandTestClass {

    @Before
    public void setup() {
        setupMocks();
        cmd = new Find();
    }

    @Test
    public void testFindTypical() {
        // given
//        when(dbManager.getTableHeader("user")).thenReturn(new String[]{"id", "name", "password"});
        when(dbManager.getTableHeader("user")).thenReturn(new LinkedHashSet<String>(Arrays.asList("id", "name", "password")));
        when(dbManager.getAllContent("user")).thenReturn(new LinkedList<List>(Arrays.asList(
                new ArrayList(Arrays.asList("1", "Vasya", "sobaka")),
                new ArrayList(Arrays.asList("2", "Manya", "12345"))
        )));
//        when(dbManager.getAllContent("user")).thenReturn(new Object[][]{{"1", "Vasya", "sobaka"}, {"2", "Manya", "12345"}});
        // when
        cmd.process("find|user");
        // then
        assertOutput( "" +
                "+------+---------+------------+\n" +
                "+  id  +  name   +  password  +\n" +
                "+------+---------+------------+\n" +
                "+  1   +  Vasya  +   sobaka   +\n" +
                "+  2   +  Manya  +   12345    +\n" +
                "+------+---------+------------+\n");
    }

    @Test
    public void testFindNull() {
        // given
        when(dbManager.getTableHeader("user")).thenReturn(new LinkedHashSet<String>(Arrays.asList("id", "name", "password")));
        when(dbManager.getAllContent("user")).thenReturn(new LinkedList<List>(Arrays.asList(
                new ArrayList(Arrays.asList("1", "Vasya", null)),
                new ArrayList(Arrays.asList("2", "Manya", null))
        )));

        // when
        cmd.process("find|user");
        // then
        assertOutput( "" +
                "+------+---------+------------+\n" +
                "+  id  +  name   +  password  +\n" +
                "+------+---------+------------+\n" +
                "+  1   +  Vasya  +   [null]   +\n" +
                "+  2   +  Manya  +   [null]   +\n" +
                "+------+---------+------------+\n"
        );
    }

    @Test
    public void testFindEmpty() {
        // given
        when(dbManager.getTableHeader("user")).thenReturn(new LinkedHashSet<String>(Arrays.asList("id", "name", "password")));
        when(dbManager.getAllContent("user")).thenReturn(new LinkedList<List>());
        // when
        cmd.process("find|user");
        // then
        assertOutput( "" +
                "+------+--------+------------+\n" +
                "+  id  +  name  +  password  +\n" +
                "+------+--------+------------+\n"
        );
    }

    @Test
    public void testFindOneColumn() {
        // given
        when(dbManager.getTableHeader("user")).thenReturn(new LinkedHashSet<String>(Arrays.asList("name")));
        when(dbManager.getAllContent("user")).thenReturn(new LinkedList<List>(Arrays.asList(
                new ArrayList(Arrays.asList("Vasya")),
                new ArrayList(Arrays.asList("Manya"))
        )));
        // when
        cmd.process("find|user");
        // then
        assertOutput( "" +
                "+---------+\n" +
                "+  name   +\n" +
                "+---------+\n" +
                "+  Vasya  +\n" +
                "+  Manya  +\n" +
                "+---------+\n"
        );
    }

}
