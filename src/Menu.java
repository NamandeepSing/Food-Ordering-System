import java.io.*;
import java.util.ArrayList;

public class Menu implements Serializable {
//    private ArrayList<Menu> subMenu;
    private static final long serialVersionUID = 1L;
    private String menuName;
    private String category;
    private double price;
    private int quantity;
    private ArrayList<Review> reviews; // Store reviews for the item

    public Menu(String menuName, String category, double price, int quantity) {
        this.menuName = menuName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.reviews = new ArrayList<>();
    }
    public void addReview(Review review) {
        reviews.add(review);
    }
    public void viewReviews() {
        if (reviews.isEmpty()) {
            System.out.println("No reviews for this item.");
        } else {
            System.out.println("Reviews for " + menuName + ":");
            for (Review review : reviews) {
                review.printReview();
            }
        }
    }
    public String getMenuName() {
        return menuName;
    }
    public String getCategory() {
        return category;
    }
    public double getPrice() {
        return price;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void printMenu() {
        System.out.println("Item Name -> " + menuName);
        System.out.println("Category -> "+category);
        System.out.println("Price -> " + price);
        System.out.println("Quantity -> "+quantity);
        System.out.println();
    }
    public static void saveMenuToFile(String filename, ArrayList<Menu> menuList) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Menu menu : menuList) {
                writer.write(menu.getMenuName() + "," + menu.getCategory() + "," + menu.getPrice() + "," + menu.getQuantity());
                writer.newLine();
            }
        }
    }

    public static ArrayList<Menu> loadMenuFromFile(String filename) throws IOException {
        ArrayList<Menu> menuList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Menu menu = new Menu(parts[0], parts[1], Double.parseDouble(parts[2]), Integer.parseInt(parts[3]));
                menuList.add(menu);
            }
        }
        return menuList;
    }

}
