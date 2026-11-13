package calculator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private final Calculator calc = new Calculator();

    @Test
    void basicOperations() {
        assertEquals(5, calc.add(2, 3), 1e-9);
        assertEquals(-1, calc.subtract(2, 3), 1e-9);
        assertEquals(6, calc.multiply(2, 3), 1e-9);
        assertEquals(2.5, calc.divide(5, 2), 1e-9);
    }

    @Test
    void divisionByZero() {
        assertThrows(ArithmeticException.class, () -> calc.divide(10, 0));
    }

    @Test
    void stringInsteadOfNumber() {
        assertThrows(InvalidInputException.class, () -> Main.parseNumber("abc"));
    }

    @Test
    void customExceptionForNaNAndInfinity() {
        assertThrows(InvalidInputException.class, () -> calc.add(Double.NaN, 1));
        assertThrows(InvalidInputException.class, () -> calc.multiply(1, Double.POSITIVE_INFINITY));
    }

    @Test
    void unknownOperation() {
        assertThrows(InvalidInputException.class, () -> Main.calculate(calc, 1, 2, "%"));
    }
}
