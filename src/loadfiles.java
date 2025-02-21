import java.io.*;
import java.util.*;

public class loadfiles {
    public static void main(String[] args) {
        // Sample menu items
        List<Menu> menuList = Arrays.asList(
            new Menu("Momo's", "NonVeg", 250, 10),
            new Menu("Pasta", "Veg", 150, 20),
            new Menu("Burger", "Veg", 100, 15),
            new Menu("Chicken", "NonVeg", 200, 5),
            new Menu("Paneer", "Veg", 180, 8)
        );

        // Write to menu.txt
        try (BufferedWriter menuWriter = new BufferedWriter(new FileWriter("menu.txt"))) {
            for (Menu menu : menuList) {
                menuWriter.write(menu.getMenuName() + "," + menu.getCategory() + "," + menu.getPrice() + "," + menu.getQuantity());
                menuWriter.newLine();
            }
            System.out.println("menu.txt initialized.");
        } catch (IOException e) {
            System.out.println("Error writing to menu.txt: " + e.getMessage());
        }

        // Sample orders
        ArrayList<Order> orderList = new ArrayList<>(Arrays.asList(
            new Order(1, "Naman", createOrderItems("Paneer", "1", "Burger", "1"), "Pending", true, "None"),
            new Order(2, "Mitul", createOrderItems("Burger", "1", "Chicken", "2"), "Preparing", false, "None"),
            new Order(3, "Rishabh", createOrderItems("Chicken", "1"), "Delivered", true, "spicy")
        ));


        // Write to pendingOrders.txt
        try (ObjectOutputStream orderWriter = new ObjectOutputStream(new FileOutputStream("pendingOrders.txt"))) {
            orderWriter.writeObject(orderList);
            System.out.println("pendingOrders.txt initialized.");
        } catch (IOException e) {
            System.out.println("Error writing to pendingOrders.txt: " + e.getMessage());
        }
    }

    private static Map<Menu, Integer> createOrderItems(String... itemsAndQuantities) {
        Map<Menu, Integer> orderItems = new HashMap<>();
        for (int i = 0; i < itemsAndQuantities.length; i += 2) {
            String itemName = itemsAndQuantities[i];
            int quantity = Integer.parseInt(itemsAndQuantities[i + 1]);
            Menu item = findMenu(itemName); // A method to find the menu item by name
            if (item != null) {
                orderItems.put(item, quantity);
            }
        }
        return orderItems;
    }

    // Simulating finding a menu item by name
    private static Menu findMenu(String name) {
        // Here, you should search from the actual menu list or file
        switch (name) {
            case "Pizza": return new Menu("Pizza", "NonVeg", 250, 10);
            case "Pasta": return new Menu("Pasta", "Veg", 150, 20);
            case "Burger": return new Menu("Burger", "Veg", 100, 15);
            case "Chicken": return new Menu("Chicken", "NonVeg", 200, 5);
            case "Paneer": return new Menu("Paneer", "Veg", 180, 8);
            default: return null;
        }
    }
}
