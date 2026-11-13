package dut.pd3.practics.practic2;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String source = args.length > 0 ? args[0] : "https://informer.com.ua/dut/java/pr2.csv";
        List<Transaction> transactions = TransactionCSVReader.readTransactions(source);

        TransactionReportGenerator.printBalanceReport(
                TransactionAnalyzer.calculateTotalBalance(transactions));

        String monthYear = "01-2024";
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear,
                TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear));

        TransactionReportGenerator.printTopExpensesReport(TransactionAnalyzer.findTopExpenses(transactions));

        LocalDate from = LocalDate.of(2023, 12, 1);
        LocalDate to = LocalDate.of(2023, 12, 31);
        TransactionReportGenerator.printExtremesReport(from + " — " + to,
                TransactionAnalyzer.findLargestExpense(transactions, from, to),
                TransactionAnalyzer.findSmallestExpense(transactions, from, to));

        TransactionReportGenerator.printCategoryReport(TransactionAnalyzer.expensesByCategory(transactions));
        TransactionReportGenerator.printMonthReport(TransactionAnalyzer.expensesByMonth(transactions));
    }
}
