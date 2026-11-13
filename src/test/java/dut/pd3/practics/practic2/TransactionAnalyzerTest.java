package dut.pd3.practics.practic2;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TransactionAnalyzerTest {

    @Test
    void testCalculateTotalBalance() {
        List<Transaction> t = Arrays.asList(
                new Transaction("01-01-2023", 100.0, "Дохід"),
                new Transaction("02-01-2023", -50.0, "Витрата"),
                new Transaction("03-01-2023", 150.0, "Дохід"));
        assertEquals(200.0, TransactionAnalyzer.calculateTotalBalance(t), "Розрахунок загального балансу неправильний");
    }

    @Test
    void testCountTransactionsByMonth() {
        List<Transaction> t = Arrays.asList(
                new Transaction("01-02-2023", 50.0, "Дохід"),
                new Transaction("15-02-2023", -20.0, "Витрата"),
                new Transaction("05-03-2023", 100.0, "Дохід"));
        assertEquals(2, TransactionAnalyzer.countTransactionsByMonth(t, "02-2023"));
        assertEquals(1, TransactionAnalyzer.countTransactionsByMonth(t, "03-2023"));
    }

    @Test
    void testReadCsvFromFile() throws IOException {
        String path = Path.of("src/test/resources/pr2.csv").toString();
        List<Transaction> t = TransactionCSVReader.readTransactions(path);
        assertEquals(14, t.size(), "Заголовок має бути пропущений");
        assertEquals(new Transaction("05-12-2023", -450.0, "Сільпо"), t.get(0));
        assertEquals("Кав'ярня", t.get(6).getDescription());
    }

    @Test
    void testParseSkipsHeaderAndBadLines() {
        List<Transaction> t = TransactionCSVReader.parse(
                List.of("Дата,Сума,Категорія", "", "bad", "01-01-2024,abc,X", "02-01-2024,10.5,Тест"));
        assertEquals(List.of(new Transaction("02-01-2024", 10.5, "Тест")), t);
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> t = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            t.add(new Transaction(String.format("%02d-01-2024", i), -i * 100.0, "Кат" + i));
        }
        t.add(new Transaction("20-01-2024", 99999.0, "Дохід"));
        List<Transaction> top = TransactionAnalyzer.findTopExpenses(t);
        assertEquals(10, top.size());
        assertEquals(-1200.0, top.get(0).getAmount());
        assertEquals(-300.0, top.get(9).getAmount());
        assertTrue(top.stream().allMatch(x -> x.getAmount() < 0), "Доходи не мають потрапляти у витрати");
    }

    @Test
    void testLargestAndSmallestExpenseInPeriod() throws IOException {
        List<Transaction> t = TransactionCSVReader.readTransactions("src/test/resources/pr2.csv");
        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        assertEquals("Оренда квартири", TransactionAnalyzer.findLargestExpense(t, from, to).orElseThrow().getDescription());
        assertEquals("Аптека", TransactionAnalyzer.findSmallestExpense(t, from, to).orElseThrow().getDescription());
        assertTrue(TransactionAnalyzer.findLargestExpense(t, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 31)).isEmpty());
    }

    @Test
    void testExpensesByCategoryAndMonth() throws IOException {
        List<Transaction> t = TransactionCSVReader.readTransactions("src/test/resources/pr2.csv");
        Map<String, Double> byCat = TransactionAnalyzer.expensesByCategory(t);
        assertEquals(950.0, byCat.get("Сільпо"));
        assertEquals("Оренда квартири", byCat.keySet().iterator().next(), "Сортування за спаданням");
        Map<String, Double> byMonth = TransactionAnalyzer.expensesByMonth(t);
        assertEquals(List.of("12-2023", "01-2024"), List.copyOf(byMonth.keySet()));
        assertEquals(500.0, byMonth.get("01-2024"));
    }

    @Test
    void testBuildBar() {
        assertEquals("", TransactionReportGenerator.buildBar(0));
        assertEquals("*", TransactionReportGenerator.buildBar(120));
        assertEquals("***", TransactionReportGenerator.buildBar(3200));
    }
}
