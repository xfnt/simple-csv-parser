package io.github.xfnt;

import io.github.xfnt.database.Repository;
import io.github.xfnt.database.RepositoryImpl;
import io.github.xfnt.database.connection.ConnectionFactoryImpl;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Application {
    private final static Logger logger = Logger.getLogger(Application.class.getName());

    private final Repository repository;

    public Application() {
        repository = new RepositoryImpl(new ConnectionFactoryImpl());
    }

    public void run() {
        repository.createTable("data", "column_1", "column_2");
    }

    public static void main(String[] args) {
        logger.log(Level.INFO, "Program started");
        Application application = new Application();
        application.run();
    }
}
