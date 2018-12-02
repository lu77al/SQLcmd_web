package ua.kh.lual.sqlcmd.dao;

import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

public class MemoryTableTest {

    MemoryTable table;

    @Before
    public void createTable() {
        table = new MemoryTable(new LinkedHashSet<>(Arrays.asList("id", "name", "PASSWORD")));
        assertEquals( "[id, name, PASSWORD]", table.getHeader().toString());
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", "1");
        row.put("name", "Vasiliy");
        row.put("PASSWORD", "parol");
        table.insert(row);
        row.put("id", "2");
        row.put("name", "Marina");
        row.put("PASSWORD", "hook");
        table.insert(row);
        row = new LinkedHashMap<>();
        row.put("id", "3");
        row.put("PASSWORD", "PASSWORD");
        table.insert(row);
        row = new LinkedHashMap<>();
        row.put("id", "4");
        row.put("name", "Pasha");
        table.insert(row);
        row.put("id", "5");
        row.put("name", "Boris");
        row.put("PASSWORD", "parol");
        table.insert(row);
    }

    @Test
    public void getHeaderTest() {
        assertEquals( "[id, name, PASSWORD]", table.getHeader().toString());
    }

    @Test
    public void getContentTest() {
        assertEquals( "[[1, Vasiliy, parol]," +
                               " [2, Marina, hook]," +
                               " [3, null, PASSWORD]," +
                               " [4, Pasha, null]," +
                               " [5, Boris, parol]]",
        table.getContent().toString());
    }

    @Test
    public void getFilteredContentTest() {
        Map<String, Object> key = new LinkedHashMap<>();

        key.put("PASSWORD", "parol");
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

        where.put("PASSWORD", "parol");
        update.put("PASSWORD", "PASSWORD");
        table.update(update, where);
        assertEquals( "[[1, Vasiliy, PASSWORD]," +
                        " [2, Marina, hook]," +
                        " [3, null, PASSWORD]," +
                        " [4, Pasha, null]," +
                        " [5, Boris, PASSWORD]]",
                table.getContent().toString());

        where.put("PASSWORD", "1234");
        update.put("PASSWORD", "5678");
        table.update(update, where);
        assertEquals( "[[1, Vasiliy, PASSWORD]," +
                        " [2, Marina, hook]," +
                        " [3, null, PASSWORD]," +
                        " [4, Pasha, null]," +
                        " [5, Boris, PASSWORD]]",
                table.getContent().toString());

        where = new LinkedHashMap<>();
        update = new LinkedHashMap<>();

        where.put("id", "1");
        update.put("name", "Alex");
        update.put("PASSWORD", "postgres");
        table.update(update, where);
        assertEquals( "[[1, Alex, postgres]," +
                        " [2, Marina, hook]," +
                        " [3, null, PASSWORD]," +
                        " [4, Pasha, null]," +
                        " [5, Boris, PASSWORD]]",
                table.getContent().toString());
    }

    @Test
    public void deleteTest() {
        Map<String, Object> key = new LinkedHashMap<>();

        key.put("PASSWORD", "parol");
        table.delete(key);
        assertEquals( "[[2, Marina, hook]," +
                        " [3, null, PASSWORD]," +
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
