import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class OrdersPendingPage extends JFrame {
    public OrdersPendingPage() throws IOException, ClassNotFoundException {
        setTitle("Pending Orders");
        setSize(700, 500);
        setLocationRelativeTo(null); // Center the window
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Set layout for the JFrame
        setLayout(new BorderLayout(10, 10)); // Add spacing between components

        // Create a header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80)); // Dark blue background
        JLabel headerLabel = new JLabel("Pending Orders", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setForeground(Color.WHITE);
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);

        // Table for displaying pending orders
        String[] columnNames = {"Order ID", "Customer Name", "Status", "VIP"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable ordersTable = new JTable(tableModel);
        ordersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        ordersTable.setRowHeight(25);

        // Load the orders from the file
        ArrayList<Order> orderList = Order.loadOrdersFromFile("pendingOrders.txt");
        for (Order order : orderList) {
            tableModel.addRow(new Object[]{order.getOrderId(), order.getCustomerName(), order.getStatus(), order.isVIP()});
        }

        // Customize the table appearance
        ordersTable.setBackground(new Color(255, 255, 255)); // White background
        ordersTable.setSelectionBackground(new Color(52, 152, 219)); // Blue selection background
        ordersTable.setSelectionForeground(Color.WHITE); // White text on selection

        // Add the table to a scroll pane and then to the frame
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);

        // Button to go back to the menu
        JButton viewMenuButton = new JButton("View Menu");
        viewMenuButton.setFont(new Font("Arial", Font.PLAIN, 16));
        viewMenuButton.setBackground(new Color(52, 152, 219)); // Blue background
        viewMenuButton.setForeground(Color.WHITE); // White text
        viewMenuButton.setFocusPainted(false); // Remove border when clicked
        viewMenuButton.setToolTipText("Click to view the menu");
        viewMenuButton.addActionListener(e -> {
            try {
                new MenuDisplayPage(); // Open the Menu Display Page
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            dispose(); // Close the pending orders page
        });

        // Add the button to the bottom panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(viewMenuButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Make the frame visible
        setVisible(true);
    }

}
