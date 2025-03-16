package io.github.xfnt;

import io.github.xfnt.database.RepositoryImpl;
import io.github.xfnt.database.connection.ConnectionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DatabaseTest {
    private static Connection connection;
    private static Statement statement;
    private static ConnectionFactory connectionFactory;
    private static RepositoryImpl repository;

    @BeforeAll
    public static void init() {
        connection = Mockito.mock(Connection.class);
        statement = Mockito.mock(Statement.class);
        connectionFactory = Mockito.mock(ConnectionFactory.class);
        repository = new RepositoryImpl(connectionFactory);
    }

    @Test
    public void shouldCreateTest() throws SQLException {
        when(connectionFactory.getConnection()).thenReturn(connection);
        when(connection.createStatement()).thenReturn(statement);
        when(statement.executeUpdate(anyString())).thenReturn(1);

        int code = repository.createTable("data", "field_1", "field_2", "field_3");
        assertEquals(1, code);

        verify(statement).executeUpdate("CREATE TABLE IF NOT EXISTS data (field_1 TEXT, field_2 TEXT, field_3 TEXT);");
    }
}
