package financeanalyzer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        String source = "https://informer.com.ua/dut/java/pr2.csv";
        List<Transaction> transactions = TransactionCSVReader.readTransactions(source);

        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        String monthYear = "01-2024";
        int transactionsCount = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, transactionsCount);

        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);

        LocalDate from = LocalDate.of(2024, 1, 1);
        LocalDate to = LocalDate.of(2024, 12, 31);
        Optional<Transaction> largest = TransactionAnalyzer.findLargestExpense(transactions, from, to);
        Optional<Transaction> smallest = TransactionAnalyzer.findSmallestExpense(transactions, from, to);
        TransactionReportGenerator.printExpenseExtremes(from, to, largest, smallest);

        TransactionReportGenerator.printExpensesChart("Витрати по категоріях",
                TransactionAnalyzer.sumExpensesByCategory(transactions));
        TransactionReportGenerator.printExpensesChart("Витрати по місяцях",
                TransactionAnalyzer.sumExpensesByMonth(transactions));

        for (Transaction transaction : transactions) {
            System.out.println(transaction);
        }
    }
}