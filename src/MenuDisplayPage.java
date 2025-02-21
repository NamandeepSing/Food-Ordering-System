import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;

public class MenuDisplayPage extends JFrame {
    public MenuDisplayPage() throws IOException {
        setTitle("Canteen Menu");
        setSize(700, 500);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));  // Add spacing between components

        // Create a top header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80)); // Dark blue background
        JLabel headerLabel = new JLabel("Welcome to the Canteen Menu", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Create the table to display menu items
        String[] columnNames = {"Name", "Category", "Price", "Quantity"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable menuTable = new JTable(tableModel);
        menuTable.setFont(new Font("Arial", Font.PLAIN, 14));
        menuTable.setRowHeight(25);

        // Load menu items from the file
        ArrayList<Menu> menuList = Menu.loadMenuFromFile("menu.txt");
        for (Menu menu : menuList) {
            tableModel.addRow(new Object[]{menu.getMenuName(), menu.getCategory(), menu.getPrice(), menu.getQuantity()});
        }

        // Customize the table design
        menuTable.setBackground(new Color(255, 255, 255)); // White background for table
        menuTable.setSelectionBackground(new Color(52, 152, 219)); // Blue selection color
        menuTable.setSelectionForeground(Color.WHITE); // White text on selection

        // Add the table to the center of the layout
        JScrollPane scrollPane = new JScrollPane(menuTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button to view pending orders
        JButton viewOrdersButton = new JButton("View Pending Orders");
        viewOrdersButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewOrdersButton.setBackground(new Color(52, 152, 219)); // Blue background
        viewOrdersButton.setForeground(Color.WHITE); // White text
        viewOrdersButton.setFocusPainted(false); // Remove border when clicked
        viewOrdersButton.setToolTipText("Click to view pending orders");
        viewOrdersButton.addActionListener(this::onViewOrdersButtonClick);

        // Add the button to the south (bottom) panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(viewOrdersButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void onViewOrdersButtonClick(ActionEvent e) {
        try {
            new OrdersPendingPage(); // Open the OrdersPendingPage window
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        dispose(); // Close the current MenuDisplayPage window
    }

}
