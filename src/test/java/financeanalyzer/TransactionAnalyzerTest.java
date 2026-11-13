package financeanalyzer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TransactionAnalyzerTest {

    @Test
    public void testCalculateTotalBalance() {
        Transaction transaction1 = new Transaction("2023-01-01", 100.0, "Дохід");
        Transaction transaction2 = new Transaction("2023-01-02", -50.0, "Витрата");
        Transaction transaction3 = new Transaction("2023-01-03", 150.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        double result = TransactionAnalyzer.calculateTotalBalance(transactions);

        Assertions.assertEquals(200.0, result, "Розрахунок загального балансу неправильний");
    }

    @Test
    public void testCountTransactionsByMonth() {
        Transaction transaction1 = new Transaction("01-02-2023", 50.0, "Дохід");
        Transaction transaction2 = new Transaction("15-02-2023", -20.0, "Витрата");
        Transaction transaction3 = new Transaction("05-03-2023", 100.0, "Дохід");
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2, transaction3);

        int countFeb = TransactionAnalyzer.countTransactionsByMonth(transactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(transactions, "03-2023");

        Assertions.assertEquals(2, countFeb, "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(1, countMar, "Кількість транзакцій за березень неправильна");
    }

    @Test
    public void testFindTopExpenses() {
        List<Transaction> transactions = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            transactions.add(new Transaction("2024-01-01", -i * 10.0, "Витрата " + i));
        }
        transactions.add(new Transaction("2024-01-02", 5000.0, "Дохід"));

        List<Transaction> top = TransactionAnalyzer.findTopExpenses(transactions);

        Assertions.assertEquals(10, top.size(), "Має бути рівно 10 витрат");
        Assertions.assertEquals(-120.0, top.get(0).getAmount(), "Перша має бути найбільша витрата");
        Assertions.assertEquals(-30.0, top.get(9).getAmount(), "Остання має бути десята за розміром");
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0), "Доходів у списку бути не повинно");
    }

    @Test
    public void testLargestAndSmallestExpenseByPeriod() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("2024-01-10", -300.0, "A"),
                new Transaction("2024-01-20", -50.0, "B"),
                new Transaction("2024-02-10", -900.0, "C"),
                new Transaction("2024-01-15", 1000.0, "Дохід")
        );
        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 1, 31);

        Optional<Transaction> largest = TransactionAnalyzer.findLargestExpense(transactions, from, to);
        Optional<Transaction> smallest = TransactionAnalyzer.findSmallestExpense(transactions, from, to);

        Assertions.assertEquals(-300.0, largest.get().getAmount(), "Найбільша витрата за січень неправильна");
        Assertions.assertEquals(-50.0, smallest.get().getAmount(), "Найменша витрата за січень неправильна");
    }

    @Test
    public void testLargestExpenseEmptyPeriod() {
        List<Transaction> transactions = List.of(new Transaction("2024-01-10", -300.0, "A"));

        Optional<Transaction> result = TransactionAnalyzer.findLargestExpense(
                transactions, LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31));

        Assertions.assertTrue(result.isEmpty(), "За період без витрат результату бути не повинно");
    }

    @Test
    public void testSumExpensesByCategoryAndMonth() {
        List<Transaction> transactions = Arrays.asList(
                new Transaction("2024-01-10", -300.0, "Food"),
                new Transaction("2024-01-20", -200.0, "Food"),
                new Transaction("2024-02-10", -100.0, "Taxi"),
                new Transaction("2024-02-11", 1000.0, "Salary")
        );

        Map<String, Double> byCategory = TransactionAnalyzer.sumExpensesByCategory(transactions);
        Map<String, Double> byMonth = TransactionAnalyzer.sumExpensesByMonth(transactions);

        Assertions.assertEquals(500.0, byCategory.get("Food"), "Сума по категорії Food неправильна");
        Assertions.assertEquals(100.0, byCategory.get("Taxi"), "Сума по категорії Taxi неправильна");
        Assertions.assertFalse(byCategory.containsKey("Salary"), "Доходи не мають потрапляти у витрати");
        Assertions.assertEquals(500.0, byMonth.get("2024-01"), "Сума за січень неправильна");
        Assertions.assertEquals(100.0, byMonth.get("2024-02"), "Сума за лютий неправильна");
    }
}