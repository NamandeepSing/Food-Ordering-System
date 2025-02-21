import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        File menuFile = new File("menu.txt");
        File ordersFile = new File("pendingOrders.txt");

        if (!menuFile.exists() || !ordersFile.exists()) {
            System.out.println("Initializing files...");
            loadfiles.main(null);  // Initialize files with sample data
        }
        ArrayList<Menu> menulist = new ArrayList<>();
        ArrayList<Order> orderList = new ArrayList<>();
        HashMap<String, ArrayList<Order>> orderMap = new HashMap<>();
        PriorityQueue<Order> pendingOrders = new PriorityQueue<>((o1, o2) -> {
            // VIP customers get priority, otherwise by order ID (FIFO for regular)
            if (o1.isVIP() && !o2.isVIP()) return -1;
            else if (!o1.isVIP() && o2.isVIP()) return 1;
            return Integer.compare(o1.getOrderId(), o2.getOrderId());
        });
        try {
            menulist = Menu.loadMenuFromFile("menu.txt");
            orderList = Order.loadOrdersFromFile("pendingOrders.txt");
            for (Order order : orderList) {
                orderMap.putIfAbsent(order.getCustomerName(), new ArrayList<>());
                orderMap.get(order.getCustomerName()).add(order);
                pendingOrders.add(order);
            }
        } catch (ClassNotFoundException | IOException e) {
            System.out.println("Error loading data from files: " + e.getMessage());
        }
        int total_sales = 0;
        int total_orders = 0;
        Map<String, Integer> itemFrequency = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("1) Admin Interface");
            System.out.println("2) Customer Interface");
            System.out.println("3) For Interface");
            int input = sc.nextInt();

            if (input == 1) {
                System.out.println("1) Menu Management");
                System.out.println("2) Order Management");
                System.out.println("3) Report Sales");
                int n1 = sc.nextInt();
                if (n1 == 1) {
                    System.out.println("1) Add New Item");
                    System.out.println("2) Update Item");
                    System.out.println("3) Delete Item");
                    System.out.println("4) Modify Prices");
                    System.out.println("5) Update Availability");
                    int n = sc.nextInt();

                    if (n == 1) {
                        System.out.println("Enter the name of the item:");
                        sc.nextLine(); // Consume newline
                        String name = sc.nextLine();
                        System.out.println("Enter the category of the item:");
                        String category = sc.nextLine();
                        System.out.println("Enter the price of the item:");
                        int price = sc.nextInt();
                        System.out.println("Enter the quantity of the item:");
                        int quantity = sc.nextInt();
                        Menu m = new Menu(name, category, price, quantity);
                        menulist.add(m);
                        System.out.println("Item Added Successfully");
                    } else if (n == 2) {
                        // Update Item Logic
                    } else if (n == 3) {
                        System.out.println("Enter the name of the item to delete:");
                        sc.nextLine(); // Consume newline
                        String name = sc.nextLine();
                        Menu m1 = findMenu(menulist, name);
                        if (m1 != null) {
                            menulist.remove(m1);
                            System.out.println("Item deleted from menu successfully.");
                            PriorityQueue<Order> updatedPendingOrders = new PriorityQueue<>(pendingOrders);
                            for (Order order : pendingOrders) {
                                if (order.getOrderItems().containsKey(m1)) {
                                    order.setStatus("Denied"); // Update the status
                                    System.out.println("Order ID " + order.getOrderId() + " has been denied due to item removal.");
                                    updatedPendingOrders.remove(order); // Remove from pending orders
                                }
                            }
                            pendingOrders = updatedPendingOrders;
                        } else {
                            System.out.println("Menu item not found.");
                        }

                    } else if (n == 4) {
                        System.out.println("Enter the name of the item to modify price:");
                        sc.nextLine(); // Consume newline
                        String name = sc.nextLine();
                        Menu m1 = findMenu(menulist, name);
                        if (m1 != null) {
                            System.out.println("Enter new price:");
                            int price = sc.nextInt();
                            m1.setPrice(price);
                            System.out.println("Price updated successfully.");
                        } else {
                            System.out.println("Menu item not found.");
                        }
                    } else if (n == 5) {
                        System.out.println("Enter the name of the item to update quantity:");
                        sc.nextLine(); // Consume newline
                        String name = sc.nextLine();
                        Menu m1 = findMenu(menulist, name);
                        if (m1 != null) {
                            System.out.println("Enter new quantity:");
                            int quantity = sc.nextInt();
                            m1.setQuantity(quantity);
                            System.out.println("Quantity updated successfully.");
                        } else {
                            System.out.println("Menu item not found.");
                        }
                    } else {
                        System.out.println("Enter a valid option.");
                    }
                    try {
                        Menu.saveMenuToFile("menu.txt", menulist);
                    } catch (IOException e) {
                        System.out.println("Error saving menu: " + e.getMessage());
                    }
                } else if (n1 == 2) {
                    System.out.println("1) View Pending orders");
                    System.out.println("2) Update Next Order");
                    System.out.println("3) Process Refunds");
                    int orderOption = sc.nextInt();
                    if (orderOption == 1) {
                        System.out.println("Pending orders");
                        for (Order o : pendingOrders) {
                            o.printOrderDetails();
                        }
                    } else if (orderOption == 2) {
                        Order nextOrder = pendingOrders.peek(); // View next order without removing
                        System.out.println("Next Pending Order:");
                        nextOrder.printOrderDetails();

                        System.out.println("Enter new status for this order (e.g., Preparing, Out for Delivery, Delivered):");
                        sc.nextLine(); // Consume newline
                        String status = sc.nextLine();
                        nextOrder.setStatus(status);
                        System.out.println("Order status updated to: " + status);

                        // If the order is delivered, update total sales, total orders, and item frequency
                        if (status.equalsIgnoreCase("Delivered")) {
                            total_orders++;
                            for (Map.Entry<Menu, Integer> entry : nextOrder.getOrderItems().entrySet()) {
                                Menu item = entry.getKey();
                                int quantity = entry.getValue();
                                total_sales += item.getPrice() * quantity;
                                itemFrequency.put(item.getMenuName(), itemFrequency.getOrDefault(item.getMenuName(), 0) + quantity);
                            }
                            pendingOrders.poll(); // Remove the delivered order from the queue
                            System.out.println("Order has been delivered and removed from pending list.");
                        }
                    } else if (orderOption == 3) {
                        System.out.println("Enter Order ID for refund:");
                        int orderId = sc.nextInt();

                        boolean refundProcessed = false;
                        for (Order order : pendingOrders) {
                            if (order.getOrderId() == orderId && order.getStatus().equalsIgnoreCase("Cancelled")) {
                                order.processRefund();
                                refundProcessed = true;
                                break;
                            }
                        }
                        if (!refundProcessed) {
                            System.out.println("Order not found or not eligible for refund.");
                        }
                    }
                } else if (n1 == 3) {
                    System.out.println("Total sales -> " + total_sales);
                    System.out.println("Total Orders -> " + total_orders);
                    String item = "";
                    int count = 0;
                    for (Map.Entry<String, Integer> mapelement : itemFrequency.entrySet()) {
                        String key = mapelement.getKey();
                        int value = mapelement.getValue();
                        if (count < value) {
                            count = value;
                            item = key;
                        }
                    }
                    System.out.println("Most selling item is -> " + item);
                }
            } else if (input == 2) {
                boolean isLoggedIn = false;
//                String currentUser = "";
                String username1 = "";
                String password1 = "";

                while (!isLoggedIn) {
                    System.out.println("=== Customer Login ===");
                    System.out.println("1) Login");
                    System.out.println("2) Register");
                    int authChoice = sc.nextInt();
                    sc.nextLine(); // Consume newline

                    if (authChoice == 1) { // Login
                        System.out.println("Enter Username:");
                        String username = sc.nextLine();
                        username1 = username;
                        System.out.println("Enter Password:");
                        String password = sc.nextLine();
                        password1 = password;

                        try {
                            if (User.authenticateUser(username, password)) {
                                System.out.println("Login Successful! Welcome, " + username);
                                isLoggedIn = true;
                                username1 = username;
                            } else {
                                System.out.println("Invalid username or password. Try again.");
                            }
                        } catch (IOException e) {
                            System.out.println("Error during login: " + e.getMessage());
                        }
                    } else if (authChoice == 2) { // Register
                        System.out.println("Enter a New Username:");
                        String newUsername = sc.nextLine();
                        username1 = newUsername;
                        System.out.println("Enter a Password:");
                        String newPassword = sc.nextLine();
                        password1 = newPassword;

                        try {
                            if (User.saveUser(newUsername, newPassword)) {
                                System.out.println("Registration Successful! You can now log in.");
                            } else {
                                System.out.println("Username already exists. Try a different one.");
                            }
                        } catch (IOException e) {
                            System.out.println("Error during registration: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Invalid choice. Try again.");
                    }
                }
                Cart cart = new Cart();
                System.out.println("1) Browse Menu");
                System.out.println("2) Cart Operations");
                System.out.println("3) Order Tracking");
                System.out.println("4) Review");
                int n = sc.nextInt();

                if (n == 1) {
                    System.out.println("1) View all Items");
                    System.out.println("2) Search Item");
                    System.out.println("3) Filter By Category");
                    System.out.println("4) Sort By Price");
                    int n1 = sc.nextInt();

                    if (n1 == 1) {
                        for (Menu m : menulist) {
                            m.printMenu();
                        }
                    } else if (n1 == 2) {
                        System.out.println("Enter the name of the item to search:");
                        sc.nextLine();
                        String name = sc.nextLine();
                        Menu m1 = findMenu(menulist, name);
                        if (m1 != null) {
                            m1.printMenu();
                        } else {
                            System.out.println("Menu item not found.");
                        }
                    } else if (n1 == 3) {
                        System.out.println("Enter category (veg/nonveg):");
                        sc.nextLine();
                        String category = sc.nextLine();
                        for (Menu m : menulist) {
                            if (m.getCategory().equalsIgnoreCase(category)) {
                                m.printMenu();
                            }
                        }
                    } else if (n1 == 4) {
                        menulist.sort(Comparator.comparingDouble(Menu::getPrice));

                        System.out.println("Items sorted by price:");
                        for (Menu m : menulist) {
                            m.printMenu();
                        }
                    }
                } else if (n == 2) {

//                    isLoggedIn = false;
                    while (true) {
                        System.out.println("1) Add item to Cart");
                        System.out.println("2) Modify Quantities");
                        System.out.println("3) Remove Items");
                        System.out.println("4) View Total");
                        System.out.println("5) Checkout Process");

                        int n1 = sc.nextInt();
                        if (n1 == 1) {
                            System.out.println("Enter the name of item to add:");
                            sc.nextLine();
                            String name = sc.nextLine();
                            Menu m1 = findMenu(menulist, name);
                            System.out.println("Enter the Quantity of the item:");
                            int quantity = sc.nextInt();

                            if (m1 != null) {
                                if (m1.getQuantity() >= quantity) {
                                    cart.addItem(m1, quantity);
                                    m1.setQuantity(m1.getQuantity() - quantity);
                                    System.out.println("Item added successfully.");
                                } else {
                                    System.out.println("Quantity available is " + m1.getQuantity());
                                }
                            } else {
                                System.out.println("Menu item not found.");
                            }
                        } else if (n1 == 2) {
                            System.out.println("Enter the Item to modify");
                            sc.nextLine();
                            String name = sc.nextLine();
                            Menu m1 = findMenu(menulist, name);

                            if (m1 != null) {
                                System.out.println("Enter new Quantity:");
                                int quantity = sc.nextInt();
                                Menu cartItem = cart.returnItem(m1);

                                if (cartItem != null && m1.getQuantity() + cartItem.getQuantity() >= quantity) {
                                    cart.addItem(m1, quantity);
                                    m1.setQuantity(m1.getQuantity() + cartItem.getQuantity() - quantity);
                                    System.out.println("Successfully Changed Quantity.");
                                } else {
                                    System.out.println("Quantity available is " + m1.getQuantity());
                                }
                            } else {
                                System.out.println("Menu item not found.");
                            }
                        } else if (n1 == 3) {
                            System.out.println("Enter the Item to Remove:");
                            sc.nextLine();
                            String name = sc.nextLine();
                            Menu m1 = findMenu(menulist, name);

                            if (m1 != null) {
                                int quantity = cart.returnItem(m1).getQuantity();
                                cart.removeItem(m1);
                                m1.setQuantity(m1.getQuantity() + quantity);
                                System.out.println("Item Removed Successfully.");
                            } else {
                                System.out.println("Cart item not found.");
                            }
                        } else if (n1 == 4) {
                            System.out.println("Total is -> " + cart.viewTotal());
                        } else if (n1 == 5) {
                            System.out.println("Total is -> " + cart.viewTotal());
                            System.out.println("Enter your name:");
                            sc.nextLine();
                            String customerName = sc.nextLine();

                            System.out.println("Are you a VIP customer? (yes/no)");
                            String vipStatus = sc.nextLine();
                            boolean isVIP = vipStatus.equalsIgnoreCase("yes");

                            System.out.println("Enter any special requests (or press Enter to skip):");
                            String specialRequest = sc.nextLine();

                            Map<Menu, Integer> orderItems = cart.getCartItems();
                            Order newOrder = new Order(pendingOrders.size() + 1, customerName, orderItems, "Received", isVIP, specialRequest);
                            pendingOrders.add(newOrder);

                            ArrayList<Order> customerOrders = orderMap.getOrDefault(customerName, new ArrayList<>());
                            customerOrders.add(newOrder);
                            orderMap.put(customerName, customerOrders);

                            System.out.println("Order placed successfully. Your Order ID is " + newOrder.getOrderId());
                            break;
                        }
                        try {
                            Order.saveOrdersToFile("pendingOrders.txt", new ArrayList<>(pendingOrders));
                        } catch (IOException e) {
                            System.out.println("Error saving orders: " + e.getMessage());
                        }
                    }
                } else if (n == 3) {
                    System.out.println("1 View order status");
                    System.out.println("2 Cancel order");
                    System.out.println("3 Order History");
                    int in = sc.nextInt();
                    if (in == 1) { // View order status
                        System.out.println("Enter the customer Name:");
                        sc.nextLine(); // Consume newline
                        String name = sc.nextLine();

                        ArrayList<Order> customerOrders = orderMap.get(name);
                        if (customerOrders != null && !customerOrders.isEmpty()) {
                            System.out.println("Order status for " + name + ":");
                            for (Order o : customerOrders) {
                                System.out.println("Order ID: " + o.getOrderId() + ", Status: " + o.getStatus());
                            }
                        } else {
                            System.out.println("No orders found for " + name);
                        }
                    } else if (in == 2) {
                        System.out.println("Enter the customer Name:");
                        sc.nextLine();
                        String name = sc.nextLine();
                        ArrayList<Order> orders = orderMap.get(name);
                        if (orders != null && !orders.isEmpty()) {
                            System.out.println("Enter Order ID to cancel:");
                            int orderId = sc.nextInt();
                            boolean canceled = false;

                            for (Order order : orders) {
                                if (order.getOrderId() == orderId && !order.getStatus().equals("Completed")) {
                                    order.setStatus("Canceled");
                                    canceled = true;
                                    System.out.println("Order canceled successfully.");
                                    break;
                                }
                            }

                            if (!canceled) {
                                System.out.println("Order not found or already processed.");
                            }
                        } else {
                            System.out.println("No active orders to cancel.");
                        }
                    } else if (in == 3) {
                        // Display order history and reorder if necessary
                        List<Order> userOrders = UserHistory.loadOrderHistory(username1);
                        System.out.println("Order History for " + username1 + ":");
                        for (Order order : userOrders) {
                            System.out.println("Order ID: " + order.getOrderId() + ", Items: " + order.getOrderItems() + ", Total Price: " + order.getTotalPrice());
                        }

// Reorder functionality
                        System.out.println("Do you want to reorder a previous meal? (yes/no)");
                        String reorderChoice = sc.nextLine();
                        if (reorderChoice.equalsIgnoreCase("yes")) {
                            System.out.println("Enter the Order ID of the meal you want to reorder:");
                            int reorderId = sc.nextInt();
                            Order selectedOrder = null;
                            for (Order order : userOrders) {
                                if (order.getOrderId() == reorderId) {
                                    selectedOrder = order;
                                    break;
                                }
                            }

                            if (selectedOrder != null) {
                                // Reorder the meal and add to the new order list
                                Order newOrder = new Order(userOrders.size() + 1, username1, selectedOrder.getOrderItems(), "Received", selectedOrder.isVIP(), selectedOrder.getSpecialRequest());

                                // Add the new order to the pending orders list (or queue)
                                pendingOrders.add(newOrder);  // Assuming you are using a PriorityQueue for pending orders
                                userOrders.add(newOrder);  // Add the reordered meal to the user's order history

                                System.out.println("Reordered successfully. New Order ID: " + newOrder.getOrderId());

                                // Save the updated order history to the user's file
                                UserHistory.saveOrderHistory(username1, userOrders);

                                // Save the updated pending orders to the pending orders file
                                try {
                                    Order.saveOrdersToFile("pendingOrders.txt", new ArrayList<>(pendingOrders));
                                    System.out.println("Updated pending orders saved to file.");
                                } catch (IOException e) {
                                    System.out.println("Error saving pending orders: " + e.getMessage());
                                }
                            } else {
                                System.out.println("Order ID not found in history.");
                            }
                        }

                    } else if (n == 4) {
                        System.out.println("1) Provide Review");
                        System.out.println("2) View Reviews");
                        int in1 = sc.nextInt();
                        if (in1 == 1) {
                            System.out.println("Enter Your Name");
                            sc.nextLine();
                            String name = sc.nextLine();
                            System.out.println("Enter the item name to review");
                            String item = sc.nextLine();
                            Menu m1 = findMenu(menulist, item);
                            if (m1 != null) {
                                System.out.println("Enter the Review");
                                String review = sc.nextLine();
                                Review review1 = new Review(name, review);
                                m1.addReview(review1);
                                System.out.println("Review Added successfully.");
                            } else {
                                System.out.println("Menu item not found.");
                            }
                        } else if (in1 == 2) {
                            System.out.println("Enter the name of item to see reviews");
                            sc.nextLine();
                            String name = sc.nextLine();
                            Menu m1 = findMenu(menulist, name);
                            if (m1 != null) {
                                m1.viewReviews();
                            } else {
                                System.out.println("Menu item not found.");
                            }
                        }
                    }
                }
            } else if (input == 3) {
                // Launch GUI (Display latest data)
                System.out.println("Launching GUI...");
                SwingUtilities.invokeLater(() -> {
                    try {
                        new MenuDisplayPage();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
//                break;  // Exit the loop after showing the GUI
            }
        }
    }
    public static Menu findMenu(List<Menu> menulist, String name) {
        for (Menu m : menulist) {
            if (m.getMenuName().equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null; // Return null if the item is not found
    }
}
