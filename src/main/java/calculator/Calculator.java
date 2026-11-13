package calculator;

public class Calculator {

    public double add(double a, double b) {
        check(a, b);
        return a + b;
    }

    public double subtract(double a, double b) {
        check(a, b);
        return a - b;
    }

    public double multiply(double a, double b) {
        check(a, b);
        return a * b;
    }

    public double divide(double a, double b) {
        check(a, b);
        if (b == 0) {
            throw new ArithmeticException("Ділення на нуль неможливе");
        }
        return a / b;
    }

    // Перевірка вхідних значень: NaN та Infinity недопустимі
    private void check(double a, double b) {
        if (!Double.isFinite(a) || !Double.isFinite(b)) {
            throw new InvalidInputException("Числа мають бути скінченними (не NaN і не Infinity)");
        }
    }
}
