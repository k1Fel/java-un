package financeanalyzer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

class TransactionCSVReaderTest {

    @Test
    public void testParseTransactions() {
        List<String> lines = List.of(
                "date,amount,description",
                "2024-01-05,-100.5,Food",
                "",
                "bad line",
                "2024-01-06,2000,Salary, bonus"
        );

        List<Transaction> result = TransactionCSVReader.parseTransactions(lines);

        Assertions.assertEquals(2, result.size(), "Заголовок, порожні та некоректні рядки мають бути пропущені");
        Assertions.assertEquals(-100.5, result.get(0).getAmount());
        Assertions.assertEquals("Food", result.get(0).getDescription());
        Assertions.assertEquals("Salary, bonus", result.get(1).getDescription());
    }

    @Test
    public void testReadTransactionsFromFile(@TempDir Path tempDir) throws IOException {
        Path file = tempDir.resolve("test.csv");
        Files.writeString(file, "date,amount,description\n2024-01-05,-100.5,Food\n2024-01-06,2000,Salary\n");

        List<Transaction> result = TransactionCSVReader.readTransactions(file.toString());

        Assertions.assertEquals(2, result.size(), "Кількість прочитаних транзакцій неправильна");
        Assertions.assertEquals("2024-01-05", result.get(0).getDate());
        Assertions.assertEquals(2000.0, result.get(1).getAmount());
    }
}