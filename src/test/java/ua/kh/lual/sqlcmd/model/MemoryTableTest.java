package ua.kh.lual.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class MemoryTableTest {

    MemoryTable table;

    @Before
    public void createTable() {
        table = new MemoryTable(new LinkedHashSet<>(Arrays.asList("id", "name", "password")));
        assertEquals( "[id, name, password]", table.getHeader().toString());
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", "1");
        row.put("name", "Vasiliy");
        row.put("password", "parol");
        table.insert(row);
        row.put("id", "2");
        row.put("name", "Marina");
        row.put("password", "hook");
        table.insert(row);
        row = new LinkedHashMap<>();
        row.put("id", "3");
        row.put("password", "password");
        table.insert(row);
        row = new LinkedHashMap<>();
        row.put("id", "4");
        row.put("name", "Pasha");
        table.insert(row);
        row.put("id", "5");
        row.put("name", "Boris");
        row.put("password", "parol");
        table.insert(row);
    }

    @Test
    public void getHeaderTest() {
        assertEquals( "[id, name, password]", table.getHeader().toString());
    }

    @Test
    public void getContentTest() {
        assertEquals( "[[1, Vasiliy, parol]," +
                               " [2, Marina, hook]," +
                               " [3, null, password]," +
                               " [4, Pasha, null]," +
                               " [5, Boris, parol]]",
        table.getContent().toString());
    }

    @Test
    public void getFilteredContentTest() {
        Map<String, Object> key = new LinkedHashMap<>();

        key.put("password", "parol");
        assertEquals( "[[1, Vasiliy, parol], [5, Boris, parol]]",
                table.getFilteredContent(key).toString());

        key.put("id", "1");
        assertEquals( "[[1, Vasiliy, parol]]",
                table.getFilteredContent(key).toString());

        key.put("id", "2");
        assertEquals( "[]",
                table.getFilteredContent(key).toString());

        key = new LinkedHashMap<>();
        key.put("id", "5");
        assertEquals( "[[5, Boris, parol]]",
                table.getFilteredContent(key).toString());

        key.put("id", "6");
        assertEquals( "[]",
                table.getFilteredContent(key).toString());
    }

    @Test
    public void clearTest() {
        table.clear();
        assertEquals( "[]",
                table.getContent().toString());
    }

    @Test
    public void updateTest() {
        Map<String, Object> where = new LinkedHashMap<>();
        Map<String, Object> update = new LinkedHashMap<>();

        where.put("password", "parol");
        update.put("password", "password");
        table.update(update, where);
        assertEquals( "[[1, Vasiliy, password]," +
                        " [2, Marina, hook]," +
                        " [3, null, password]," +
                        " [4, Pasha, null]," +
                        " [5, Boris, password]]",
                table.getContent().toString());

        where.put("password", "1234");
        update.put("password", "5678");
        table.update(update, where);
        assertEquals( "[[1, Vasiliy, password]," +
                        " [2, Marina, hook]," +
                        " [3, null, password]," +
                        " [4, Pasha, null]," +
                        " [5, Boris, password]]",
                table.getContent().toString());

        where = new LinkedHashMap<>();
        update = new LinkedHashMap<>();

        where.put("id", "1");
        update.put("name", "Alex");
        update.put("password", "postgres");
        table.update(update, where);
        assertEquals( "[[1, Alex, postgres]," +
                        " [2, Marina, hook]," +
                        " [3, null, password]," +
                        " [4, Pasha, null]," +
                        " [5, Boris, password]]",
                table.getContent().toString());
    }

    @Test
    public void deleteTest() {
        Map<String, Object> key = new LinkedHashMap<>();

        key.put("password", "parol");
        table.delete(key);
        assertEquals( "[[2, Marina, hook]," +
                        " [3, null, password]," +
                        " [4, Pasha, null]]",
                table.getContent().toString());

        key = new LinkedHashMap<>();
        key.put("id", "3");
        key.put("name", null);
        table.delete(key);
        assertEquals( "[[2, Marina, hook]," +
                        " [4, Pasha, null]]",
                table.getContent().toString());

        key = new LinkedHashMap<>();
        key.put("id", "2");
        table.delete(key);
        assertEquals( "[[4, Pasha, null]]",
                table.getContent().toString());

        key.put("id", "8");
        table.delete(key);
        assertEquals( "[[4, Pasha, null]]",
                table.getContent().toString());
    }

}
