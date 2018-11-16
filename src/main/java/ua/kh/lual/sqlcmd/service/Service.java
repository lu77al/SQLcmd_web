package ua.kh.lual.sqlcmd.service;

import java.util.List;

public interface Service {
    List<String> commandsList();

    void connect(String dbName, String userName, String password);
}
