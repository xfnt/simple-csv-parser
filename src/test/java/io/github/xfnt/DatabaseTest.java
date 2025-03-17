package io.github.xfnt;

import io.github.xfnt.database.DatabaseException;
import io.github.xfnt.database.RepositoryImpl;
import io.github.xfnt.database.connection.ConnectionFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static io.github.xfnt.constant.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class DatabaseTest {
    private Connection connection;
    private Statement statement;
    private ConnectionFactory connectionFactory;
    private RepositoryImpl repository;

    private final String TABLE = "data";
    private final HashMap<String, String> data = new HashMap<>();

    @BeforeEach
    public void init() {
        connection = Mockito.mock(Connection.class);
        statement = Mockito.mock(Statement.class);
        connectionFactory = Mockito.mock(ConnectionFactory.class);
        repository = new RepositoryImpl(connectionFactory);

        data.put("col_1", "value_1");
        data.put("col_2", "value_2");
    }

    @Test
    public void shouldCreateTable() throws SQLException {
        when(connectionFactory.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenReturn(1);

        int code = repository.createTable(TABLE, "field_1", "field_2", "field_3");
        assertEquals(1, code);

        verify(statement).executeUpdate("CREATE TABLE IF NOT EXISTS data (field_1 TEXT, field_2 TEXT, field_3 TEXT);");
    }

    @Test
    public void shouldThrowExceptionWhenCreateTable() throws SQLException {
        when(connectionFactory.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenThrow(new SQLException());

        Throwable error = assertThrows(RuntimeException.class, () -> {
            repository.createTable(TABLE, "field_1", "field_2", "field_3");
        });
        assertEquals(CREATE_TABLE_EXCEPTION_MESSAGE_TEMPLATE.formatted(TABLE), error.getMessage());

        verify(statement).executeUpdate("CREATE TABLE IF NOT EXISTS data (field_1 TEXT, field_2 TEXT, field_3 TEXT);");
    }

    @Test
    public void shouldInsertValues() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connectionFactory.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        repository.insert(TABLE, data);

        verify(preparedStatement).setString(1, "value_1");
        verify(preparedStatement).setString(2, "value_2");
    }

    @Test
    public void shouldThrowExceptionWhenInsertEmptyValues() throws SQLException {
        Throwable error = assertThrows(IllegalArgumentException.class, () -> {
            repository.insert(TABLE, Map.of());
        });
        assertEquals(NO_DATA_EXCEPTION_MESSAGE, error.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenInsertData() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        when(connectionFactory.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException());

        Throwable error = assertThrows(DatabaseException.class, () -> {
            repository.insert(TABLE, data);
        });
        assertEquals(INSERT_VALUES_EXCEPTION_MESSAGE, error.getMessage());

        verify(preparedStatement).setString(1, "value_1");
        verify(preparedStatement).setString(2, "value_2");
    }
}