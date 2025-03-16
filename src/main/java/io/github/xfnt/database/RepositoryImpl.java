package io.github.xfnt.database;

import io.github.xfnt.database.connection.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RepositoryImpl implements Repository {
    private final static Logger logger = Logger.getLogger(RepositoryImpl.class.getName());

    private final ConnectionFactory connectionFactory;

    public RepositoryImpl(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @Override
    public int createTable(String tableName, String... fields) {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append(" (");
        for(int i = 0; i < fields.length; i++) {
            if(i == fields.length - 1) {
                sqlBuilder.append(fields[i]).append(" ").append("TEXT");
            }else {
                sqlBuilder.append(fields[i]).append(" ").append("TEXT").append(", ");
            }
        }
        sqlBuilder.append(");");
        logger.log(Level.INFO, "Execute: {0}", sqlBuilder.toString());

        try(Connection connection = connectionFactory.getConnection();
            Statement statement = connection.createStatement()) {
            int code = statement.executeUpdate(sqlBuilder.toString());
            logger.log(Level.INFO, "Table {0} created!", tableName);
            return code;
        }catch (SQLException e) {
            return -1;
        }
    }
}
