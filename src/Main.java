import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Створення категорій
        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");
        // Створення об'єктів класу Product з вказівкою категорії
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Ноутбук", 19999.99, "Ноутбук для роботи та ігор", electronics));
        products.add(new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном", smartphones));
        products.add(new Product(3, "Навушники", 2499.00, "Бездротові навушники", accessories));
        // Створення кошику
        Cart cart = new Cart();
        List<Order> history = new ArrayList<>();
        while (true) {
            System.out.println("\nВиберіть опцію:");
            System.out.println("1 - Переглянути список товарів");
            System.out.println("2 - Додати товар до кошика");
            System.out.println("3 - Видалити товар з кошика");
            System.out.println("4 - Переглянути кошик");
            System.out.println("5 - Зробити замовлення");
            System.out.println("6 - Історія замовлень");
            System.out.println("7 - Пошук");
            System.out.println("0 - Вийти");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    for (Product p : products) System.out.println(p);
                    break;
                case 2: {
                    System.out.println("Введіть ID товару для додавання до кошика:");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    Product found = null;
                    for (Product p : products) {
                        if (p.getId() == id) found = p;
                    }
                    if (found != null) {
                        cart.addProduct(found);
                        System.out.println("Додано: " + found.getName());
                    } else {
                        System.out.println("Товар з таким ID не знайдено");
                    }
                    break;
                }
                case 3: {
                    System.out.println("Введіть ID товару для видалення з кошика:");
                    int idr = scanner.nextInt();
                    scanner.nextLine();
                    Product found = null;
                    for (Product p : products) {
                        if (p.getId() == idr) found = p;
                    }
                    if (found != null) {
                        cart.removeProduct(found);
                        System.out.println("Видалено: " + found.getName());
                    } else {
                        System.out.println("Товар з таким ID не знайдено");
                    }
                    break;
                }
                case 4:
                    System.out.println(cart);
                    break;
                case 5:
                    if (cart.getProducts().isEmpty()) {
                        System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
                    } else {
                        Order order = new Order(cart);
                        System.out.println("Замовлення оформлено:");
                        System.out.println(order);
                        history.add(order);
                        cart.clear(); // Метод для очищення кошика
                    }
                    break;
                case 6:
                    if (history.isEmpty()) {
                        System.out.println("Історія порожня.");
                    } else {
                        for (Order o : history) System.out.println(o + "\n");
                    }
                    break;
                case 7: {
                    System.out.println("Введіть назву або категорію:");
                    String q = scanner.nextLine().toLowerCase();
                    boolean anyFound = false;
                    for (Product p : products) {
                        if (p.getName().toLowerCase().contains(q)
                                || p.getCategory().getName().toLowerCase().contains(q)) {
                            System.out.println(p);
                            anyFound = true;
                        }
                    }
                    if (!anyFound) System.out.println("Нічого не знайдено.");
                    break;
                }
                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;
                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
                    break;
            }
        }
    }
}