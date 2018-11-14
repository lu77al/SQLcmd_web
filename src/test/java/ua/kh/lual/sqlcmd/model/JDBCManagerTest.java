package ua.kh.lual.sqlcmd.model;

public class JDBCManagerTest extends DBManagerTest {
    @Override
    public void setup() {
        dbManager = new JDBCManager();
        dbManager.connect(database, user, password);
    }
}
