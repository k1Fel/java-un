package financeanalyzer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {
    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("yyyy-MM-dd"),
            DateTimeFormatter.ofPattern("dd-MM-yyyy"),
            DateTimeFormatter.ofPattern("yyyy/MM/dd"),
            DateTimeFormatter.ofPattern("dd/MM/yyyy")
    };
    private static final DateTimeFormatter MONTH_YEAR = DateTimeFormatter.ofPattern("MM-yyyy");
    private static final DateTimeFormatter YEAR_MONTH = DateTimeFormatter.ofPattern("yyyy-MM");

    public static double calculateTotalBalance(List<Transaction> transactions) {
        return transactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public static int countTransactionsByMonth(List<Transaction> transactions, String monthYear) {
        if (monthYear == null || monthYear.isBlank()) {
            return 0;
        }

        String target = monthYear.trim().replace('/', '-');
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = parseDate(transaction.getDate());
            if (date != null
                    && (target.equals(date.format(MONTH_YEAR)) || target.equals(date.format(YEAR_MONTH)))) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparingDouble(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static List<Transaction> findExpensesByPeriod(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = parseDate(t.getDate());
                    return date != null && !date.isBefore(from) && !date.isAfter(to);
                })
                .collect(Collectors.toList());
    }

    public static Optional<Transaction> findLargestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return findExpensesByPeriod(transactions, from, to).stream()
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Optional<Transaction> findSmallestExpense(List<Transaction> transactions, LocalDate from, LocalDate to) {
        return findExpensesByPeriod(transactions, from, to).stream()
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> sumExpensesByCategory(List<Transaction> transactions) {
        Map<String, Double> result = new TreeMap<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAmount() < 0) {
                result.merge(transaction.getDescription(), -transaction.getAmount(), Double::sum);
            }
        }
        return result;
    }

    public static Map<String, Double> sumExpensesByMonth(List<Transaction> transactions) {
        Map<String, Double> result = new TreeMap<>();
        for (Transaction transaction : transactions) {
            LocalDate date = parseDate(transaction.getDate());
            if (transaction.getAmount() < 0 && date != null) {
                result.merge(date.format(YEAR_MONTH), -transaction.getAmount(), Double::sum);
            }
        }
        return result;
    }

    private static LocalDate parseDate(String dateText) {
        if (dateText == null || dateText.isBlank()) {
            return null;
        }
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                return LocalDate.parse(dateText.trim(), formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }
}