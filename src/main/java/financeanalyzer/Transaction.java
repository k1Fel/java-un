package financeanalyzer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Transaction {
    private final String date;
    private final double amount;
    private final String description;
}