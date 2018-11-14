package ua.kh.lual.sqlcmd.model;

import org.junit.Before;
import org.junit.Test;
import ua.kh.lual.sqlcmd.NamesAndPasswords;

import java.util.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public abstract class DBManagerTest {

    protected static final String database = NamesAndPasswords.database;
    protected static final String user = NamesAndPasswords.user;
    protected static final String password = NamesAndPasswords.password;
    protected static final String table = NamesAndPasswords.table;

    protected DatabaseManager dbManager;

    private void createTable() {
        try {
            dbManager.createTable(table, new LinkedHashSet<String>(Arrays.asList("name", "password")));
        } catch (Exception e) {
            // Just catch
        }
    }

    private void dropTable() {
        try {
            dbManager.dropTable(table);
        } catch (Exception e) {
            // Just catch
        }
    }

    private void fillTable() throws Exception {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("name", "Vasiliy");
        row.put("password", "parol");
        dbManager.insert(table, row);
        row.put("name", "Marina");
        row.put("password", "hook");
        dbManager.insert(table, row);
    }

    @Before
    public abstract void setup();

    @Test
    public void testConnect() {
        assertTrue(dbManager.isConnected());
    }

    @Test
    public void testCreateTable() {
        boolean failed = false;
        try {
            dbManager.createTable(table, new LinkedHashSet<String>(Arrays.asList("name", "password")));
        } catch (Exception e) {
            failed = true;
        }
        dropTable();
        assertFalse(failed);
    }


    @Test
    public void testDropTable() {
        createTable();
        boolean failed = false;
        try {
            dbManager.dropTable(table);
        } catch (Exception e) {
            failed = true;
        }
        assertFalse(failed);
    }

    @Test
    public void testGetTableNames() {
        createTable();
        String tables = dbManager.getTableNames().toString();
        dropTable();
        assertTrue(tables.indexOf(table) != -1);
    }

    @Test
    public void testGetTableHeader() {
        createTable();
        String header = dbManager.getTableHeader(table).toString();
        dropTable();
        assertEquals("[name, password]", header);
    }

    @Test
    public void testInsert() {
        createTable();
        boolean failed = false;
        try {
            fillTable();
        } catch (Exception e) {
            failed = true;
        }
        dropTable();
        assertFalse(failed);
    }

    @Test
    public void testGetAllContent() {
        createTable();
        try {
            fillTable();
        } catch (Exception e) {
            // Just catch
        }
        List<List> content = dbManager.getAllContent(table);
        dropTable();
        assertEquals("[[Vasiliy, parol], [Marina, hook]]", content.toString());
    }

    @Test
    public void testGetFilteredContent() {
        createTable();
        try {
            fillTable();
        } catch (Exception e) {
            // Just catch
        }
        Map<String, Object> key = new LinkedHashMap<>();
        key.put("name", "Vasiliy");
        List<List> content = dbManager.getFilteredContent(table, key);
        dropTable();
        assertEquals("[[Vasiliy, parol]]",content.toString());
    }

    @Test
    public void testClearTable() {
        createTable();
        try {
            fillTable();
            dbManager.clearTable(table);
        } catch (Exception e) {
            // Just catch
        }
        List<List> content = dbManager.getAllContent(table);
        dropTable();
        assertEquals("[]", content.toString());
    }

    @Test
    public void testDelete() {
        createTable();
        try {
            fillTable();
        } catch (Exception e) {
            // Just catch
        }
        Map<String, Object> key = new LinkedHashMap<>();
        key.put("name", "Vasiliy");
        dbManager.delete(table, key);
        List<List> content = dbManager.getAllContent(table);
        dropTable();
        assertEquals("[[Marina, hook]]", content.toString());
    }

    @Test
    public void testUpdate() {
        createTable();
        try {
            fillTable();
        } catch (Exception e) {
            // Just catch
        }
        Map<String, Object> key = new LinkedHashMap<>();
        key.put("name", "Vasiliy");
        Map<String, Object> update = new LinkedHashMap<>();
        update.put("password", "ChertPoberi");
        dbManager.update(table, update, key);
        List<List> content = dbManager.getAllContent(table);
        dropTable();
        assertEquals( 40, content.toString().length());
        assertTrue(content.toString().contains("[Marina, hook]"));
        assertTrue(content.toString().contains("[Vasiliy, ChertPoberi]"));
    }

    @Test
    public void testDisconnect() {
        dbManager.disconnect();
        assertFalse(dbManager.isConnected());
    }

}
