package io.github.xfnt.database;

public interface Repository {

    int createTable(String tableName, String ...fields);
}
