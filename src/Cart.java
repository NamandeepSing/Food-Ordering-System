import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    private Map<Menu, Integer> cartItems; // Holds items and their quantities

    public Cart() {
        cartItems = new HashMap<>();
    }

    public String addItem(Menu item, int quantity) {
        if (item.getQuantity() < quantity) {
            return "Item out of stock"; // Return feedback when item is unavailable
        }
        if (cartItems.containsKey(item)) {
            cartItems.put(item, cartItems.get(item) + quantity); // Update quantity
        } else {
            cartItems.put(item, quantity); // Add new item
        }
        item.setQuantity(item.getQuantity() - quantity); // Deduct from stock
        return "Item added to cart"; // Return success message
    }


    public boolean checkout() {
        for (Map.Entry<Menu, Integer> entry : cartItems.entrySet()) {
            if (entry.getKey().getQuantity() <= 0) {
                System.out.println("Item is out of stock.");
                return false; // Prevent checkout
            }
        }
        System.out.println("Order placed successfully.");
        return true;
    }


    public void removeItem(Menu item) {
        cartItems.remove(item);
    }

    public String modifyItemQuantity(Menu item, int quantity) {
        if (quantity < 0) {
            return "Quantity cannot be negative.";
        }

        if (cartItems.containsKey(item)) {
            cartItems.put(item, quantity);  // Update the quantity in the map
            return "Quantity updated successfully.";
        } else {
            return "Item not found in the cart.";
        }
    }



    public double viewTotal() {
        double total = 0.0;

        for (Map.Entry<Menu, Integer> entry : cartItems.entrySet()) {
            Menu item = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("Calculating total: " + item.getMenuName() + " * " + quantity + " @ " + item.getPrice());
            total += item.getPrice() * quantity;
        }

        System.out.println("Total price: " + total);
        return total;
    }




    public Map<Menu, Integer> getCartItems() {
        return cartItems;
    }
    public Menu returnItem(Menu item) {
        for (Menu menuItem : cartItems.keySet()) {
            if (menuItem.equals(item)) {
                return menuItem;
            }
        }
        return null;
    }
    public void saveCartToFile(String userId) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("cart_" + userId + ".dat"))) {
            oos.writeObject(cartItems);
        }
    }

    // Load the cart from a file
    public static Map<Menu, Integer> loadCartFromFile(String userId) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("cart_" + userId + ".dat"))) {
            return (Map<Menu, Integer>) ois.readObject();
        }
    }
}
