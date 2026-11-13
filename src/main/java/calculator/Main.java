package calculator;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Введіть перше число: ");
            double a = parseNumber(scanner.nextLine());

            System.out.print("Введіть друге число: ");
            double b = parseNumber(scanner.nextLine());

            System.out.print("Введіть операцію (+, -, *, /): ");
            String op = scanner.nextLine().trim();

            System.out.println("Результат: " + calculate(calculator, a, b, op));
        } catch (InvalidInputException e) {
            System.out.println("Некоректні дані: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("Помилка обчислення: " + e.getMessage());
        } finally {
            System.out.println("Обробку запиту завершено.");
        }
    }

    static double parseNumber(String s) {
        try {
            return Double.parseDouble(s.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new InvalidInputException("'" + s.trim() + "' не є числом");
        }
    }

    static double calculate(Calculator c, double a, double b, String op) {
        switch (op) {
            case "+": return c.add(a, b);
            case "-": return c.subtract(a, b);
            case "*": return c.multiply(a, b);
            case "/": return c.divide(a, b);
            default: throw new InvalidInputException("Невідома операція: '" + op + "'");
        }
    }
}
