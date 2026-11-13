package dut.pd3.practics.practic2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public abstract class TransactionCSVReader {

    public static List<Transaction> readTransactions(String source) throws IOException {
        return parse(readLines(source));
    }

    public static List<String> readLines(String source) throws IOException {
        if (source.startsWith("http://") || source.startsWith("https://")) {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(URI.create(source).toURL().openStream(), StandardCharsets.UTF_8))) {
                return br.lines().toList();
            }
        }
        return Files.readAllLines(Path.of(source), StandardCharsets.UTF_8);
    }

    public static List<Transaction> parse(List<String> lines) {
        List<Transaction> result = new ArrayList<>();
        for (String line : lines) {
            if (line == null || line.isBlank()) continue;
            String[] v = line.replace("﻿", "").split(",", 3);
            if (v.length < 3) continue;
            try {
                result.add(new Transaction(v[0].trim(), Double.parseDouble(v[1].trim()), v[2].trim()));
            } catch (NumberFormatException e) {
                // заголовок "Дата,Сума,Категорія" або зіпсований рядок — пропускаємо
            }
        }
        return result;
    }
}
