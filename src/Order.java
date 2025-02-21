import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Order implements Comparable<Order>, Serializable {
    private static final long serialVersionUID = 1L;  // Ensures backward compatibility during deserialization

    private int orderId;
    private String customerName;
    private Map<Menu, Integer> orderItems;
    private String status;
    private boolean isRefunded;
    private boolean isVIP;
    private String specialRequest;

    public Order(int orderId, String customerName, Map<Menu, Integer> orderItems, String status, boolean isVIP, String specialRequest) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.orderItems = orderItems;
        this.status = status;
        this.isRefunded = false;
        this.isVIP = isVIP;
        this.specialRequest = specialRequest;
    }

    // Getter and Setter methods...

    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public Map<Menu, Integer> getOrderItems() {
        return orderItems;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isRefunded() {
        return isRefunded;
    }

    public void processRefund() {
        if (!isRefunded) {
            isRefunded = true;
            System.out.println("Refund processed for order ID: " + orderId);
        } else {
            System.out.println("Order ID " + orderId + " is already refunded.");
        }
    }

    public boolean isVIP() {
        return isVIP;
    }

    public String getSpecialRequest() {
        return specialRequest;
    }

    @Override
    public int compareTo(Order other) {
        if (this.isVIP && !other.isVIP) {
            return -1;
        } else if (!this.isVIP && other.isVIP) {
            return 1;
        } else {
            return Integer.compare(this.orderId, other.orderId);
        }
    }

    public void printOrderDetails() {
        System.out.println("Order ID: " + orderId + ", Customer: " + customerName + ", Status: " + status);
        if (isVIP) System.out.println("Customer Type: VIP");
        System.out.println("Special Request: " + specialRequest);
    }

    public static void saveOrdersToFile(String filename, ArrayList<Order> orders) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
            oos.writeObject(orders); // Serialize the ArrayList of Order objects
        }
    }

    public static ArrayList<Order> loadOrdersFromFile(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
            Object readObject = ois.readObject();
            if (readObject instanceof ArrayList) { // Check the type
                return (ArrayList<Order>) readObject;
            } else {
                throw new ClassCastException("File content is not an ArrayList<Order>");
            }
        }

    }
    public static void saveOrderHistory(String username, List<Order> orders) throws IOException {
        String filename = "orderHistory_" + username + ".txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename, true))) {
            for (Order order : orders) {
                bw.write(order.toCSV());
                bw.newLine();
            }
        }
    }

    // Convert Order to CSV
    public String toCSV() {
        // Convert order to a CSV format
        return orderId + "," + customerName + "," + status + "," + isVIP + "," + formatOrderItems(orderItems);
    }

    private String formatOrderItems(Map<Menu, Integer> orderItems) {
        // Convert items to "item=quantity" format
        List<String> items = new ArrayList<>();
        for (Map.Entry<Menu, Integer> entry : orderItems.entrySet()) {
            items.add(entry.getKey().getMenuName() + "=" + entry.getValue());
        }
        return String.join(";", items);
    }

//    public String getTotalPrice() {
//    }
    public double getTotalPrice() {
        double totalPrice = 0;
        for (Map.Entry<Menu, Integer> entry : orderItems.entrySet()) {
            Menu menuItem = entry.getKey();
            int quantity = entry.getValue();
            totalPrice += menuItem.getPrice() * quantity; // Price * Quantity for each item
        }
        return totalPrice;
    }
}
