package io.github.xfnt.database;

import java.util.Map;

public interface Repository {

    int createTable(String table, String ...fields);

    void insert(String table, Map<String, String> data);
}
