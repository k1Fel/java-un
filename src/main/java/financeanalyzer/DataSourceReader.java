package financeanalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public abstract class DataSourceReader {

    public static List<String> readLines(String source) {
        List<String> lines = new ArrayList<>();
        if (source == null || source.isBlank()) {
            return lines;
        }

        try (BufferedReader br = openReader(source)) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Не вдалося прочитати джерело: " + e.getMessage());
        }
        return lines;
    }

    private static BufferedReader openReader(String source) throws IOException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            return new BufferedReader(new InputStreamReader(
                    URI.create(source).toURL().openStream(), StandardCharsets.UTF_8));
        }
        return Files.newBufferedReader(Path.of(source), StandardCharsets.UTF_8);
    }
}