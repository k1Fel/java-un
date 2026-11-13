package financeanalyzer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class TransactionReportGenerator {
    private static final double MONEY_PER_STAR = 1000.0;

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction expense : topExpenses) {
            System.out.println(expense.getDescription() + ": " + expense.getAmount());
        }
    }

    public static void printExpenseExtremes(LocalDate from, LocalDate to,
                                            Optional<Transaction> largest, Optional<Transaction> smallest) {
        System.out.println("Витрати за період " + from + " - " + to + ":");
        System.out.println("Найбільша: " + describe(largest));
        System.out.println("Найменша: " + describe(smallest));
    }

    public static void printExpensesChart(String title, Map<String, Double> expenses) {
        System.out.println(title + " (1 * = " + (int) MONEY_PER_STAR + " грн):");
        for (Map.Entry<String, Double> entry : expenses.entrySet()) {
            int stars = (int) Math.round(entry.getValue() / MONEY_PER_STAR);
            System.out.printf("%-20s %10.2f %s%n", entry.getKey(), entry.getValue(), "*".repeat(stars));
        }
    }

    private static String describe(Optional<Transaction> transaction) {
        return transaction
                .map(t -> t.getDescription() + ": " + t.getAmount() + " (" + t.getDate() + ")")
                .orElse("немає витрат за цей період");
    }
}