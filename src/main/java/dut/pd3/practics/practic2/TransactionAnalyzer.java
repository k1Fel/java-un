package dut.pd3.practics.practic2;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter MONTH_FMT = DateTimeFormatter.ofPattern("MM-yyyy");

    public static LocalDate parseDate(Transaction t) {
        return LocalDate.parse(t.getDate(), DATE_FMT);
    }

    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream().mapToDouble(Transaction::getAmount).sum();
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        return (int) transactions.stream()
                .filter(t -> parseDate(t).format(MONTH_FMT).equals(monthYear))
                .count();
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static List<Transaction> expensesInPeriod(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate d = parseDate(t);
                    return !d.isBefore(from) && !d.isAfter(to);
                })
                .collect(Collectors.toList());
    }

    public static Optional<Transaction> findLargestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return expensesInPeriod(transactions, from, to).stream()
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Optional<Transaction> findSmallestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return expensesInPeriod(transactions, from, to).stream()
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> expensesByCategory(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(Transaction::getDescription,
                        Collectors.summingDouble(t -> -t.getAmount())))
                .entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (a, b) -> a, java.util.LinkedHashMap::new));
    }

    public static Map<String, Double> expensesByMonth(List<Transaction> transactions) {
        Map<java.time.YearMonth, Double> sorted = new TreeMap<>();
        for (Transaction t : transactions) {
            if (t.getAmount() < 0) {
                sorted.merge(java.time.YearMonth.from(parseDate(t)), -t.getAmount(), Double::sum);
            }
        }
        Map<String, Double> result = new java.util.LinkedHashMap<>();
        sorted.forEach((ym, sum) -> result.put(ym.format(MONTH_FMT), sum));
        return result;
    }
}
