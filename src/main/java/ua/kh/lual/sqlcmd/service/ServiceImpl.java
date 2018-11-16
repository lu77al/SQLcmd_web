package ua.kh.lual.sqlcmd.service;

import java.util.Arrays;
import java.util.List;

public class ServiceImpl implements Service {
    @Override
    public List<String> commandsList() {
        return Arrays.asList("help", "connect", "tables");
    }
}
