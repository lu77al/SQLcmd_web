package ua.kh.lual.sqlcmd.dao;

public class MemoryDBManagerTest extends DBManagerTest {
    @Override
    public void setup() {
        dbManager = new MemoryDBManager();
        dbManager.connect(database, user, password);
    }
}
