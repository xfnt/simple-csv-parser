package io.github.xfnt.database;

import io.github.xfnt.database.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static io.github.xfnt.constant.Constants.*;

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
            throw new DatabaseException(CREATE_TABLE_EXCEPTION_MESSAGE_TEMPLATE.formatted(table), e);
        }
    }

    @Override
    public void insert(String table, Map<String, String> data) {
        if (data.isEmpty()) {
            throw new IllegalArgumentException(NO_DATA_EXCEPTION_MESSAGE);
        }

        List<String> keys = new ArrayList<>(new TreeSet<>(data.keySet()));

        String columns = String.join(", ", keys);
        String placeholders = String.join(", ", Collections.nCopies(keys.size(), "?"));

        String sql = "INSERT INTO " + table + " (" + columns + ") VALUES (" + placeholders + ")";
        logger.log(Level.INFO, "Executing SQL: {0}", sql);

        try (Connection connection = connectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (int i = 0; i < keys.size(); i++) {
                statement.setString(i + 1, data.get(keys.get(i)));
            }

            int rowsInserted = statement.executeUpdate();
            logger.log(Level.INFO, "Inserted {0} row(s) into {1}", new Object[]{rowsInserted, table});
        } catch (SQLException e) {
            throw new DatabaseException(INSERT_VALUES_EXCEPTION_MESSAGE, e);
        }
    }
}
