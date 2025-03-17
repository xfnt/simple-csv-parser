package io.github.xfnt.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.stream.Stream;

public class FileParser {
    private final Path filePath;

    public FileParser(String filePath) {
        this.filePath = Optional.of(Paths.get(filePath))
                .filter(Files::exists)
                .filter(Files::isRegularFile)
                .orElseThrow(() -> new RuntimeException("File '%s' not found".formatted(filePath)));
    }

    public long linesCount() {
        try (var lines = Files.lines(filePath)) {
            return lines.count();
        } catch (IOException e) {
            throw new RuntimeException("Error when count lines", e);
        }
    }

    public Optional<String> readLine(int lineNumber) {
        try (Stream<String> lines = Files.lines(filePath)) {
            return lines.skip(lineNumber - 1).findFirst();
        } catch (IOException e) {
            throw new RuntimeException("Error when read line", e);
        }
    }

    public Path getFilePath() {
        return filePath;
    }
}
