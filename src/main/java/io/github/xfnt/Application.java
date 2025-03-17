package io.github.xfnt;

import io.github.xfnt.database.Repository;
import io.github.xfnt.database.RepositoryImpl;
import io.github.xfnt.database.connection.ConnectionFactoryImpl;
import io.github.xfnt.parser.FileParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.IntStream;

public class Application {
    private final static Logger logger = Logger.getLogger(Application.class.getName());

    private final Repository repository;
    private final FileParser fileParser;

    public Application() {
        Configuration configuration = new Configuration();
        repository = new RepositoryImpl(new ConnectionFactoryImpl(configuration));
        fileParser = new FileParser(configuration.getFilePath());
    }

    public void run() {
        long lines = fileParser.linesCount();
        if (lines != 0) {
            // Читаем первую строку, проверяем формат и возвращаем имя таблицы
            String tableName = fileParser.readLine(1)
                    .filter(t -> t.startsWith("\"Table:\""))
                    .map(t -> t.split(","))
                    .filter(arr -> arr.length == 2)
                    .map(arr -> arr[arr.length - 1])
                    .orElseThrow(() -> new RuntimeException("Incorrect format of table"));
            logger.log(Level.INFO, "Read table from file: {0}", tableName);

            // Читаем вторую строку, проверяем формат и возвращаем массив столбцов
            String[] columns = fileParser.readLine(2)
                    .filter(t -> t.startsWith("\"Columns:\""))
                    .map(t -> t.split(","))
                    .filter(arr -> arr.length > 1)
                    .map(arr -> Arrays.copyOfRange(arr, 1, arr.length))
                    .orElseThrow(() -> new RuntimeException("Incorrect format of columns"));
            logger.log(Level.INFO, "Read columns from file: {0}", Arrays.toString(columns));

            // Создаем таблицу в БД
            repository.createTable(tableName, columns);

            // Считываем данные
            for (int i = 3; i <= lines; i++) {
                String[] data = fileParser.readLine(i)
                        .filter(t -> t.contains("\"\""))
                        .map(t -> t.split(","))
                        .filter(arr -> arr.length > 1)
                        .map(arr -> Arrays.copyOfRange(arr, 1, arr.length))
                        .orElseThrow(() -> new RuntimeException("Incorrect format of data"));
                logger.log(Level.INFO, "Read data from file: {0}", Arrays.toString(data));

                // Записываем данные в БД
                repository.insert(tableName, IntStream.range(0, columns.length)
                        .collect(HashMap::new, (m, j) -> m.put(columns[j], data[j]), Map::putAll));
            }
        } else {
            throw new RuntimeException("File '%s' is empty".formatted(fileParser.getFilePath().getFileName()));
        }
    }

    public static void main(String[] args) {
        logger.log(Level.INFO, "Program started");
        Application application = new Application();
        application.run();
    }
}
