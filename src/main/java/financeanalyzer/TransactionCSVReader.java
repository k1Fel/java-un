package financeanalyzer;

import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    public static List<Transaction> readTransactions(String source) {
        List<String> lines = DataSourceReader.readLines(source);
        return parseTransactions(lines);
    }

    public static List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();

        for (String rawLine : lines) {
            String line = rawLine.replace("\uFEFF", "").trim();
            if (line.isEmpty() || line.toLowerCase().startsWith("date")) {
                continue;
            }

            String[] values = line.split(",", 3);
            if (values.length < 3) {
                continue;
            }

            try {
                double amount = parseAmount(values[1].trim());
                transactions.add(new Transaction(values[0].trim(), amount, values[2].trim()));
            } catch (NumberFormatException e) {
                // некоректна сума - рядок пропускаємо
            }
        }
        return transactions;
    }

    private static double parseAmount(String amountText) {
        String normalized = amountText.replace(" ", "").replace("\u00A0", "");
        if (normalized.contains(",") && !normalized.contains(".")) {
            normalized = normalized.replace(',', '.');
        }
        return Double.parseDouble(normalized);
    }
}