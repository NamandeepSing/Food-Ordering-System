import java.io.*;
import java.util.*;

public class UserHistory{

    // Register new user by saving username and password to file
    public static List<Order> loadOrderHistory(String username) {
        List<Order> userOrders = new ArrayList<>();
        try {
            // Define the file path (user-specific order history file)
            File file = new File(username + "_orders.txt");
            if (file.exists()) {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
                userOrders = (List<Order>) ois.readObject();
                ois.close();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading order history: " + e.getMessage());
        }
        return userOrders;
    }

    // Save the updated order history for a specific user to a file
    public static void saveOrderHistory(String username, List<Order> userOrders) {
        try {
            File file = new File(username + "_orders.txt");
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(userOrders);
            oos.close();
        } catch (IOException e) {
            System.out.println("Error saving order history: " + e.getMessage());
        }
    }
}
