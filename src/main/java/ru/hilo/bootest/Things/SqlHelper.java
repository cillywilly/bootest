package ru.hilo.bootest.Things;

import lombok.SneakyThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    public static final SqlHelper SIEBEL = new SqlHelper(Cfg.SIEBEL);
    private static Connection connection;
    private final Cfg cfg;

    private SqlHelper(Cfg cfg) {
        this.cfg = cfg;
    }


    private void loadDriver() {
        try {
            Class.forName(cfg.driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @SneakyThrows
    public Connection getConnectionInstance(){
        if (connection != null) {
            return connection;
        } else {
            connection = createConnection();
        }
        return connection;
    }

    Connection createConnection() throws SQLException {
        loadDriver();
        Connection connection = DriverManager.getConnection(cfg.url, cfg.login, cfg.pass);
        connection.setAutoCommit(false);
        return connection;
    }

    public String select(String query, String defaultReturnValue) {
            try (Connection connection = createConnection()) {
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    String returnValue = statement.executeQuery().getString();
                    connection.commit();
                    return returnValue;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        return defaultReturnValue;
    }
}
