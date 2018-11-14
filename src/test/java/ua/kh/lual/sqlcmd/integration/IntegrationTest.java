package ua.kh.lual.sqlcmd.integration;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ua.kh.lual.sqlcmd.NamesAndPasswords;
import ua.kh.lual.sqlcmd.controller.Main;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import static org.junit.Assert.assertEquals;

public class IntegrationTest {

    private static PreparedInputStream in;
    private static ByteArrayOutputStream out;

    private static final String database = NamesAndPasswords.database;
    private static final String user = NamesAndPasswords.user;
    private static final String password = NamesAndPasswords.password;

    private String expected;

    @BeforeClass
    public static void setup() {
        in = new PreparedInputStream();
        System.setIn(in);
        ua.kh.lual.sqlcmd.view.Console.suppressFormating();
    }

    @Before
    public void clearOutput() {
        out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
    }

    @Test
    public void testExit() {
        // given
        exitApp();
        // after
        expected = "";
        // execute and check
        performTest();
    }

    @Test
    public void testUnknown() {
        // given
        in.userTypes("something");
        exitApp();
        // after
        expected =
            "something\n" +
            "Unknown command: something\n" +
            askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testTooManyParameters() {
        // given
        in.userTypes("exit|system");
        exitApp();
        // after
        expected =
                "exit|system\n" +
                "Command failed\n" +
                "Too many parameters\n" +
                "Please use format: exit\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testNotEnoughParameters() {
        // given
        in.userTypes("connect|system");
        exitApp();
        // after
        expected =
                "connect|system\n" +
                "Command failed\n" +
                "Not enough parameters\n" +
                "Please use format: connect|database|user|password\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testConnect() {
        // given
        connectDB();
        exitApp();
        // after
        expected =
            "connect|" + database + "|" + user + "|" + password + "\n" +
            "User " + user + " successfully connected to database " + database + "\n" +
            askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testConnectError() {
        // given
        in.userTypes("connect|" + database + "123|" + user + "12|234d" + password);
        exitApp();
        // after
        expected =
                "connect|sqlcmd123|postgres12|234d12345\n" +
                "Command failed\n" +
                "JDBCManager error: Can't get connection for database:sqlcmd123 user:postgres12 password:234d12345\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testHelp() {
        // given
        in.userTypes("help");
        exitApp();
        // after
        expected =
                "help\n" +
                "You can use next commands:\n" +
                "\tconnect|database|user|password\n" +
                "\t\tConnects to database database as user user with password password\n" +
                "\tmemory\n" +
                "\t\tFor switching to memory database manager realization\n" +
                "\tmain\n" +
                "\t\tFor switching to main (usually postgress) database manager realization\n"+
                "\ttables\n" +
                "\t\tPrints tables names of connected database\n" +
                "\tfind|tableName\n" +
                "\t\tPrints content of the table tableName\n" +
                "\tclear|tableName\n" +
                "\t\tClears table tableName\n" +
                "\tinsert|tableName|column1|value1|column2|value2| ... |columnN |valueN\n" +
                "\t\tInserts one new row in the table tableName\n" +
                "\t\t\tcolumn1 - name of first column to insert\n" +
                "\t\t\tvalue1  - value to insert into first column\n" +
                "\t\t\tcolumn2 - name of second column to insert\n" +
                "\t\t\tvalue2  - value to insert into second column\n" +
                "\t\t\tcolumnN - name of N's column to insert\n" +
                "\t\t\tvalueN  - value to insert into N's column\n" +
                "\tupdate|tableName|destColumn|destValue|keyColumn|keyValue\n" +
                "\t\tUpdates value of specified cells in the table tableName\tdestColumn - name of column to update\n" +
                "\t\t\tdestValue  - new value for column to update\n" +
                "\t\t\tkeyColumn  - name of column to check before update\n" +
                "\t\t\tkeyValue   - update occurs if keyValue equals actual value of keyColumn\n" +
                "\t\t\t  * data in several rows could be updated\n" +
                "\tdelete|tableName|column|value\n" +
                "\t\tDeletes rows from table tableName in which column column has value value\n" +
                "\tcreate|tableName|column1|column2|...|columnN\n" +
                "\t\tcreates table tableName with specified columns\n" +
                "\tdrop|tableName\n" +
                "\t\tDeletes table tableName from database\n" +
                "\texit\n" +
                "\t\tTerminates application\n" +
                "\t`|[n]\n" +
                "\t\tExecutes some hardcoded sequences. Can be used as additional runtime test tool\n" +
                "\t\t\tn - test to start. Now available 1 '|1\n" +
                "\t\t\twithout parameter n starts previously started test (startupDefaultTest an startup)\n" +
                "\thelp\n" +
                "\t\tPrints this brief commands summary\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testCreateCreateErrorDrop() {
        // given
        connectDB();
        createTable();
        in.userTypes("create|guineypigtable|name|password");
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() +
                "create|guineypigtable|name|password\n" +
                "Command failed\n" +
                "JDBCManager error: Can't create table \"guineypigtable\"\n" +
                askCommandReport() +
                dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testDropError() {
        // given
        connectDB();
        in.userTypes("drop|guineypigtable");
        exitApp();
        // after
        expected =
                connectReport() +
                "drop|guineypigtable\n" +
                "Command failed\n" +
                "JDBCManager error: Can't drop table \"guineypigtable\"\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testInsert() {
        // given
        connectDB();
        createTable();
        fillTable();
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() + fillReport() + dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testInsertError() {
        // given
        connectDB();
        createTable();
        in.userTypes("insert|guineypigtable|lastname|Vladimir|password|secret");
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() +
                "insert|guineypigtable|lastname|Vladimir|password|secret\n" +
                "Command failed\n" +
                "JDBCManager error: Can't insert data into table \"guineypigtable\"\n" +
                askCommandReport() +
                dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testFind() {
        // given
        connectDB();
        createTable();
        fillTable();
        in.userTypes("find|guineypigtable");
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() + fillReport() +
                "find|guineypigtable\n" +
                "+------------+------------+\n" +
                "+    name    +  password  +\n" +
                "+------------+------------+\n" +
                "+  Vladimir  +   secret   +\n" +
                "+   Margo    +   citrus   +\n" +
                "+------------+------------+\n" +
                askCommandReport() +
                dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testFindError() {
        // given
        connectDB();
        in.userTypes("find|guineypigtable");
        exitApp();
        // after
        expected =
                connectReport() +
                "find|guineypigtable\n" +
                "Command failed\n" +
                "JDBCManager error: Can't get table \"guineypigtable\" header\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testClear() {
        // given
        connectDB();
        createTable();
        fillTable();
        in.userTypes("clear|guineypigtable");
        in.userTypes("find|guineypigtable");
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() + fillReport() +
                        "clear|guineypigtable\n" +
                        "Table guineypigtable was cleared\n" +
                        askCommandReport() +
                        "find|guineypigtable\n" +
                        "+--------+------------+\n" +
                        "+  name  +  password  +\n" +
                        "+--------+------------+\n" +
                        askCommandReport() +
                        dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testClearError() {
        // given
        connectDB();
        in.userTypes("clear|guineypigtable");
        exitApp();
        // after
        expected =
                connectReport() +
                "clear|guineypigtable\n" +
                "Command failed\n" +
                "JDBCManager error: Can't clear table \"guineypigtable\"\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testDelete() {
        // given
        connectDB();
        createTable();
        fillTable();
        in.userTypes("delete|guineypigtable|name|Vladimir");
        in.userTypes("delete|guineypigtable|name|Vladimir");
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() + fillReport() +
                "delete|guineypigtable|name|Vladimir\n" +
                "+------------+------------+\n" +
                "+    name    +  password  +\n" +
                "+------------+------------+\n" +
                "+  Vladimir  +   secret   +\n" +
                "+------------+------------+\n" +
                "Rows above where deleted\n" +
                askCommandReport() +
                "delete|guineypigtable|name|Vladimir\n" +
                "Nothing matches key field. No delete performed\n" +
                askCommandReport() +
                dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testDeleteError() {
        // given
        connectDB();
        in.userTypes("delete|guineypigtable|name|Vladimir");
        exitApp();
        // after
        expected =
                connectReport() +
                "delete|guineypigtable|name|Vladimir\n" +
                "Command failed\n" +
                "JDBCManager error: Can't get table \"guineypigtable\" content\n" +
                askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testUpdate() {
        // given
        connectDB();
        createTable();
        fillTable();
        in.userTypes("update|guineypigtable|password|nevertheless|name|Margo");
        dropTable();
        exitApp();
        // after
        expected =
                connectReport() + createReport() + fillReport() +
                "update|guineypigtable|password|nevertheless|name|Margo\n" +
                "+---------+------------+\n" +
                "+  name   +  password  +\n" +
                "+---------+------------+\n" +
                "+  Margo  +   citrus   +\n" +
                "+---------+------------+\n" +
                "Rows above where updated\n" +
                askCommandReport() +
                dropReport();
        // execute and check
        performTest();
    }

    @Test
    public void testUpdateError() {
        // given
        connectDB();
        in.userTypes("update|guineypigtable|password|nevertheless|name|Margo");
        exitApp();
        // after
        expected =
                connectReport() +
                        "update|guineypigtable|password|nevertheless|name|Margo\n" +
                        "Command failed\n" +
                        "JDBCManager error: Can't get table \"guineypigtable\" content\n" +
                        askCommandReport();
        // execute and check
        performTest();
    }

    @Test
    public void testTables() {
        // given
        connectDB();
        createTable();
        in.userTypes("tables");
        dropTable();
        exitApp();
        // then
        Main.main(new String[0]);
        // after
        String output = getLog().replaceAll(System.lineSeparator(), "\n");
        int actual = 0;
        String substring = "guineypigtable";
        for (int pos = output.indexOf(substring); pos >= 0; pos = output.indexOf(substring, pos + 1)) {
            actual++;
        }
        assertEquals(5, actual);
    }

    @Test
    public void testSwitchMemoryMain() {
        // given
        in.userTypes("find|qwertypasdf");
        connectDB();
        in.userTypes("find|qwertypasdf");
        in.userTypes("memory");
        in.userTypes("find|qwertypasdf");
        in.userTypes("main");
        in.userTypes("find|qwertypasdf");
        exitApp();
        // after
        expected =
                "find|qwertypasdf\n" +
                "Command failed\n" +
                "Please connect to database before using command find|qwertypasdf\n" +
                "\tUse command connect|database|user|password\n" +
                askCommandReport() +
                connectReport() +
                "find|qwertypasdf\n" +
                "Command failed\n" +
                "JDBCManager error: Can't get table \"qwertypasdf\" header\n" +
                askCommandReport() +
                "memory\n" +
                "You have switched to memory database manager realization\n" +
                askCommandReport() +
                "find|qwertypasdf\n" +
                "Command failed\n" +
                "Please connect to database before using command find|qwertypasdf\n" +
                "\tUse command connect|database|user|password\n" +
                askCommandReport() +
                "main\n" +
                "You have switched to main database manager realization\n" +
                askCommandReport() +
                "find|qwertypasdf\n" +
                "Command failed\n" +
                "JDBCManager error: Can't get table \"qwertypasdf\" header\n" +
                askCommandReport();
        // execute and check
        performTest();
    }


    private void fillTable() {
        in.userTypes("insert|guineypigtable|name|Vladimir|password|secret");
        in.userTypes("insert|guineypigtable|name|Margo|password|citrus");
    }

    private String fillReport() {
        return  "insert|guineypigtable|name|Vladimir|password|secret\n" +
                "New data added successfully\n" +
                "\n" +
                "Enter command (help for commands list)\n" +
                "insert|guineypigtable|name|Margo|password|citrus\n" +
                "New data added successfully\n" +
                askCommandReport();
    }

    private void dropTable() {
        in.userTypes("drop|guineypigtable");
    }

    private String dropReport() {
        return  "drop|guineypigtable\n" +
                "Table guineypigtable was deleted\n" +
                askCommandReport();
    }

    private void createTable() {
        in.userTypes("create|guineypigtable|name|password" );
    }

    private String createReport() {
        return
            "create|guineypigtable|name|password\n" +
            "Table guineypigtable was created successfully\n" +
            askCommandReport();
    }

    private void connectDB() {
        in.userTypes("connect|" + database + "|" + user + "|" + password);
    }

    private String connectReport() {
       return  "connect|" + database + "|" + user + "|" + password + "\n" +
               "User " + user + " successfully connected to database " + database + "\n" +
               askCommandReport();
    }

    private void exitApp() {
        in.userTypes("exit");
    }

    private String helloReport() {
        return
            "Hello. Your are using SQLcmd application\n" +
            askCommandReport();
    }

    private String exitReport() {
        return "exit\n" +
               "Bye\n" +
               "See you later ;)\n";
    }

    private String askCommandReport() {
        return "\nEnter command (help for commands list)\n";
    }

    private void performTest() {
        Main.main(new String[0]);
        String actual = getLog().replaceAll(System.lineSeparator(), "\n");
        assertEquals(helloReport() + expected + exitReport(), actual);
    }

    private String getLog() {
        try {
            return new String(out.toByteArray(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return e.getMessage();
        }
    }

}
