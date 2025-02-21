import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

public class MainTest {

    // Create a cart for testing
    private Cart cart;
    private Menu apple;
    private Menu banana;

    @BeforeEach
    public void setUp() {
        // Initialize the cart and Menu items before each test
        cart = new Cart();
        apple = new Menu("Apple", "Fruit", 1, 0);  // Out of stock (quantity = 0)
        banana = new Menu("Banana", "Fruit", 0.5, 10); // In stock (quantity = 10)
    }

    // Scenario 1: Test for ordering out-of-stock items
    @Test
    public void testOutOfStockOrder() {
        Menu item = new Menu("Pizza", "Veg", 250, 0); // Out of stock
        Cart cart = new Cart();
        String result = cart.addItem(item, 1); // Simulate adding to cart
        assertEquals("Item out of stock", result, "Expected out-of-stock error message.");
    }


    @Test
    void testAddInStockItem() {
        String result = cart.addItem(banana, 1); // Trying to add in-stock item
        assertEquals("Item added to cart", result);  // Ensure item is added to the cart
    }

    // Scenario 2: Test for invalid login attempts (Simplified - without creating a User class)
    @Test
    void testInvalidLogin() {
        String correctUsername = "admin";
        String correctPassword = "password123";

        // Simulating login attempt with wrong password
        assertFalse(login(correctUsername, "wrongPassword"), "Login should fail with incorrect password");

        // Simulating login attempt with wrong username
        assertFalse(login("wrongUser", correctPassword), "Login should fail with incorrect username");

        // Simulating valid login
        assertTrue(login(correctUsername, correctPassword), "Login should succeed with correct username and password");
    }

    // Simulate a simple login function for the test (No actual User class used)
    private boolean login(String username, String password) {
        return username.equals("admin") && password.equals("password123");
    }

    // Scenario 3: Cart Operations
    @Test
    void testAddItemToCart() {
        Menu apple = new Menu("Apple", "Fruit", 1, 10);  // Item name, category, price, stock
        Menu banana = new Menu("Banana", "Fruit",  0.5, 20);

        Cart cart = new Cart();

        cart.addItem(apple, 2);  // Add 2 apples to the cart
        cart.addItem(banana, 3);  // Add 3 bananas to the cart

        double expectedTotal = (2) + (3 * 0.5);  // 2 apples + 3 bananas
        assertEquals(expectedTotal, cart.viewTotal(), "Total price should be calculated correctly");
    }


    @Test
    void testUpdateItemQuantity() {
        cart.addItem(banana, 3); // Add 3 bananas
        cart.modifyItemQuantity(banana, 5); // Update quantity of bananas to 5

        double expectedTotal = (5 * 0.5);  // 5 bananas
        assertEquals(expectedTotal, cart.viewTotal(), "Total price should be updated correctly after quantity modification");
    }

    @Test
    public void testModifyItemQuantity() {
        Menu apple = new Menu("Apple", "Fruit", 1.0, 10);
        Cart cart = new Cart();
        cart.addItem(apple, 2);  // Add 2 apples to the cart

        // Test negative quantity
        String result = cart.modifyItemQuantity(apple, -1);
        assertEquals("Quantity cannot be negative.", result, "System should prevent setting a negative quantity.");
    }

}
