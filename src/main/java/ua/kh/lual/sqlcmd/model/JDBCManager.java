package ua.kh.lual.sqlcmd.model;

import java.sql.*;
import java.util.*;

public class JDBCManager implements DatabaseManager {

    private Connection connection;

    @Override
    public boolean isConnected() {
        return connection != null;
    }

    @Override
    public void connect(String database, String user, String password) {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
            }
            connection = null;
        }
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new DBManagerException("Can't process databases\n" +
                     "\tPlease add postgresql-42.2.5.jar to project");
        }
        try {
            connection = DriverManager.getConnection(
                    "jdbc:postgresql:" + database, user, password);
        } catch (SQLException e) {
            connection = null;
            throw new DBManagerException(
                    String.format("Can't get connection for database:<%s> user:<%s> password:<%s>",
                    database, user, password ));
        }
    }

    @Override
    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DBManagerException("Can't disconnect form database");
        }
        connection = null;
    }

    @Override
    public Set<String> getTableNames() {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery("SELECT table_name FROM information_schema.tables" +
                                                " WHERE table_schema='public' AND table_type='BASE TABLE'"))
        {
            Set<String> tables = new LinkedHashSet<>();
            while (rs.next()) {
                tables.add(rs.getString("table_name"));
            }
            return tables;
        } catch (SQLException e) {
            throw new DBManagerException("Can't get tables names");
        }
    }

    @Override
    public Set<String> getTableHeader(String tableName) {
        String selectedTable = normalizeTableName(tableName);
        try (PreparedStatement st = connection.prepareStatement("SELECT * FROM " + selectedTable + " WHERE false")) {
            ResultSetMetaData md = st.getMetaData();
            Set<String> columnNames = new LinkedHashSet<>();
            for (int i = 1; i <= md.getColumnCount(); i++) {
                columnNames.add(md.getColumnName(i));
            }
            return columnNames;
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't get table <%s> header", selectedTable));
        }
    }

    @Override
    public List<List> getAllContent(String tableName) {
        String selectedTable = normalizeTableName(tableName);
        try {
            return getTableContent("SELECT * FROM " + selectedTable);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't get table <%s> content", selectedTable));
        }
    }

    @Override
    public void clearTable(String tableName) {
        String selectedTable = normalizeTableName(tableName);
        try {
            executeSQL("DELETE FROM " + selectedTable);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't clear table <%s>", selectedTable));
        }
    }

    @Override
    public void insert(String tableName, Map<String, Object> row) {
        String selectedTable = normalizeTableName(tableName);
        String names = prepareList("\"%s\"", row.keySet());
        String values = prepareList("'%s\'", row.values());
        try {
            executeSQL("INSERT INTO " + selectedTable +" (" + names + ") VALUES (" + values + ")");
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't insert data into table <%s>", selectedTable));
        }
    }

    @Override
    public void update(String tableName, Map<String, Object> set, Map<String, Object> where) {
        String selectedTable = normalizeTableName(tableName);
        String setList = prepareList("\"%s\" = '%s'", ", ", set.keySet(), set.values());
        String whereList = prepareList("\"%s\" = '%s'", ", ", where.keySet(), where.values());
        try {
            executeSQL("UPDATE " + selectedTable + " SET " + setList + " WHERE " + whereList);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't update table <%s>", selectedTable));
        }
    }

    @Override
    public List<List> getFilteredContent(String tableName, Map<String, Object> key) {
        String selectedTable = normalizeTableName(tableName);
        String whereList = prepareList("\"%s\" = '%s'", " AND ", key.keySet(), key.values());
        String sql = "SELECT * FROM " + selectedTable + " WHERE " + whereList;
        try {
            return getTableContent(sql);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't get table <%s> content", selectedTable));
        }
    }

    @Override
    public void delete(String tableName, Map<String, Object> key) {
        String selectedTable = normalizeTableName(tableName);
        String whereList = prepareList("\"%s\" = '%s'", " AND ", key.keySet(), key.values());
        try {
            executeSQL("DELETE FROM " + selectedTable + " WHERE " + whereList);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't delete rows form table <%s>", selectedTable));
        }
    }

    @Override
    public void createTable(String tableName, Set<String> columns) {
        String selectedTable = normalizeTableName(tableName);
        String sql = "CREATE TABLE " + selectedTable + " (" +
                     prepareList("\"%s\" text", columns) + ")";
        try {
            executeSQL(sql);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't create table <%s>", selectedTable));
        }
    }

    @Override
    public void dropTable(String tableName) {
        String selectedTable = normalizeTableName(tableName);
        try {
            executeSQL("DROP TABLE " + selectedTable);
        } catch (SQLException e) {
            throw new DBManagerException(String.format("Can't drop table <%s>", selectedTable));
        }
    }

    private String normalizeTableName(String tableName) {
        return  "\"" + tableName.toLowerCase() + "\"";
    }

    private List<List> getTableContent(String sql) throws SQLException {
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql))
        {
            int tableWidth = rs.getMetaData().getColumnCount();
            List<List> data = new LinkedList<>();
            while (rs.next()) {
                List row = new ArrayList(tableWidth);
                for (int colIndex = 1; colIndex <= tableWidth ; colIndex++) {
                    row.add(rs.getObject(colIndex));
                }
                data.add(row);
            }
            return data;
        }
    }

    private void executeSQL(String query) throws SQLException {
        try (Statement st = connection.createStatement()) {
            st.executeUpdate(query);
        }
    }

    private String prepareList(String item, String delimiter, Collection values1, Collection values2) {
        StringBuilder list = new StringBuilder();
        Iterator value2Iterator = values2.iterator();
        for (Object value1: values1) {
            Object value2 = value2Iterator.next();
            list.append(String.format(item, value1.toString(), value2.toString()));
            if (value2Iterator.hasNext()) {
                list.append(delimiter);
            }
        }
        return list.toString();
    }

    private String prepareList(String item, Collection values) {
        StringBuilder list = new StringBuilder();
        for (Object value : values) {
            list.append(String.format(item, value.toString()));
            list.append(", ");
        }
        return list.substring(0, list.length() - 2);
    }

}

