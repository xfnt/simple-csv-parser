package io.github.xfnt.database.connection;

import io.github.xfnt.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactoryImpl implements ConnectionFactory {
    private final String url;
    private final String user;
    private final String password;

    public ConnectionFactoryImpl(Configuration configuration) {
        this.url = configuration.getDatabaseUrl();
        this.user = configuration.getDatabaseUser();
        this.password = configuration.getDatabasePassword();
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
