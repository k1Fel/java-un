package dut.pd3.practics.practic2;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String date;        // формат dd-MM-yyyy
    private double amount;
    private String description; // категорія
}
