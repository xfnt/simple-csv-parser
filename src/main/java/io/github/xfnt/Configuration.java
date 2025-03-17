package io.github.xfnt;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Configuration {
    private final static Logger logger = Logger.getLogger(Configuration.class.getName());

    private final String filePath;
    private final String fileDelimiter;

    private final String databaseUrl;
    private final String databaseUser;
    private final String databasePassword;

    public Configuration() {
        Properties properties = new Properties();
        try {
            properties.load(Files.newInputStream(Paths.get("./application.properties")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        logger.log(Level.INFO, "Read program properties");
        filePath = properties.getProperty("file-path", "/");
        logger.log(Level.INFO, "Read file-path: {0}", filePath);

        fileDelimiter = properties.getProperty("file-delimiter", ";");
        logger.log(Level.INFO, "Read file-delimiter: {0}", fileDelimiter);

        databaseUrl = properties.getProperty("database-url", "");
        databaseUser = properties.getProperty("database-user", "postgres");
        databasePassword = properties.getProperty("database-password", "postgres");
        logger.log(Level.INFO, "Read database-configuration: {0}, {1}, {2}",
                new Object[]{databaseUrl, databaseUser, databasePassword});
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileDelimiter() {
        return fileDelimiter;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUser() {
        return databaseUser;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}
