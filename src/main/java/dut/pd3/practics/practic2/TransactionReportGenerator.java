package dut.pd3.practics.practic2;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/** Абстрактний клас зі статичними методами виведення звітів. */
public abstract class TransactionReportGenerator {

    /** Скільки грошей відповідає одному символу '*'. */
    public static final double MONEY_PER_STAR = 1000.0;

    public static void printBalanceReport(double totalBalance) {
        System.out.println("Загальний баланс: " + totalBalance);
    }

    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.println("Кількість транзакцій за " + monthYear + ": " + count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("10 найбільших витрат:");
        for (Transaction e : topExpenses) {
            System.out.println(e.getDescription() + ": " + e.getAmount());
        }
    }

    public static void printExtremesReport(String period, Optional<Transaction> largest, Optional<Transaction> smallest) {
        System.out.println("Витрати за період " + period + ":");
        System.out.println("  Найбільша: " + largest.map(t -> t.getDescription() + " " + t.getAmount()).orElse("немає"));
        System.out.println("  Найменша:  " + smallest.map(t -> t.getDescription() + " " + t.getAmount()).orElse("немає"));
    }

    /** Текстова гістограма: один '*' на кожні MONEY_PER_STAR грн (округлення до найближчого, мінімум 1 для >0). */
    public static String buildBar(double amount) {
        if (amount <= 0) return "";
        int stars = Math.max(1, (int) Math.round(amount / MONEY_PER_STAR));
        return "*".repeat(stars);
    }

    public static String buildChart(String title, Map<String, Double> data) {
        StringBuilder sb = new StringBuilder(title).append(" (1 * = ")
                .append((int) MONEY_PER_STAR).append(" грн)\n");
        int width = data.keySet().stream().mapToInt(String::length).max().orElse(0);
        data.forEach((k, v) -> sb.append(String.format("%-" + width + "s | %s %.2f%n", k, buildBar(v), v)));
        return sb.toString();
    }

    public static void printCategoryReport(Map<String, Double> byCategory) {
        System.out.print(buildChart("Витрати по категоріях", byCategory));
    }

    public static void printMonthReport(Map<String, Double> byMonth) {
        System.out.print(buildChart("Витрати по місяцях", byMonth));
    }
}
