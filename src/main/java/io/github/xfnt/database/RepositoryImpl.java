package io.github.xfnt.database;

import io.github.xfnt.database.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositoryImpl implements Repository {
    private final static Logger logger = Logger.getLogger(RepositoryImpl.class.getName());

    private final ConnectionFactory connectionFactory;

    public RepositoryImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public int createTable(String table, String... fields) {
        String sql = "CREATE TABLE IF NOT EXISTS %s (%s".formatted(table, String.join(" TEXT, ", fields) + " TEXT);");

        logger.log(Level.INFO, "Execute: {0}", sql);

        try (Connection connection = connectionFactory.getConnection();
             Statement statement = connection.createStatement()) {
            int code = statement.executeUpdate(sql);
            logger.log(Level.INFO, "Table {0} created!", table);
            return code;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error creating table " + table, e);
            return -1;
        }
    }

    @Override
    public void insert(String table, Map<String, String> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException("Нет данных для вставки");
        }

        StringJoiner columns = new StringJoiner(", ");
        StringJoiner values = new StringJoiner(", ");
        data.forEach((column, value) -> {
            columns.add(column);
            values.add("?");
        });

        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";
        logger.log(Level.INFO, "Executing SQL: {0}", sql);

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int index = 1;
            for (String value : data.values()) {
                statement.setString(index++, value);
            }

            int rowsInserted = statement.executeUpdate();
            logger.log(Level.INFO, "Inserted {0} row(s) into {1}", new Object[]{rowsInserted, table});
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error inserting values", e);
        }
    }
}
