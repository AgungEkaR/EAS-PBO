import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.*;
import java.util.ArrayList;
import java.awt.BorderLayout;
import javax.swing.table.DefaultTableModel;
import java.awt.GridLayout;



// Main application class
public class InventoryFinanceManagerGUI {
    private List<InventoryItem> inventory;
    private List<FinancialTransaction> transactions;
    private double totalBalance;
    private double totalProfit;

    private JFrame frame;
    private JTable inventoryTable;
    private JTable transactionTable;
    private JLabel balanceLabel;
    private JLabel profitLabel;
    private DefaultTableModel inventoryModel;
    private DefaultTableModel transactionModel;

    public InventoryFinanceManagerGUI() {
        inventory = new ArrayList<>();
        transactions = new ArrayList<>();
        totalBalance = 0.0;
        totalProfit = 0.0;
        initializeGUI();
    }

    private void initializeGUI() {
        frame = new JFrame("Inventory and Finance Manager");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        // Balance Panel
        JPanel balancePanel = new JPanel(new GridLayout(2, 1));
        balanceLabel = new JLabel("Total Balance: Rp0", JLabel.CENTER);
        profitLabel = new JLabel("Total Profit: Rp0", JLabel.CENTER);
        balancePanel.add(balanceLabel);
        balancePanel.add(profitLabel);
        frame.add(balancePanel, BorderLayout.NORTH);

        // Tabbed Pane
        JTabbedPane tabbedPane = new JTabbedPane();

        // Inventory Tab
        JPanel inventoryPanel = new JPanel(new BorderLayout());
        inventoryModel = new DefaultTableModel(new String[]{"Item", "Quantity", "Unit Price (Rp)", "Total Value (Rp)"}, 0);
        inventoryTable = new JTable(inventoryModel);
        inventoryPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

        JPanel inventoryControls = new JPanel();
        JTextField itemNameField = new JTextField(10);
        JTextField itemQuantityField = new JTextField(5);
        JTextField itemPriceField = new JTextField(7);
        JButton addItemButton = new JButton("Add Item");
        JButton sellItemButton = new JButton("Sell Item");

        inventoryControls.add(new JLabel("Name:"));
        inventoryControls.add(itemNameField);
        inventoryControls.add(new JLabel("Quantity:"));
        inventoryControls.add(itemQuantityField);
        inventoryControls.add(new JLabel("Unit Price (Rp):"));
        inventoryControls.add(itemPriceField);
        inventoryControls.add(addItemButton);
        inventoryControls.add(sellItemButton);
        inventoryPanel.add(inventoryControls, BorderLayout.SOUTH);

        addItemButton.addActionListener(e -> {
            String name = itemNameField.getText();
            int quantity;
            double price;
            try {
                quantity = Integer.parseInt(itemQuantityField.getText());
                price = Double.parseDouble(itemPriceField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid quantity or price!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double cost = quantity * price;
            if (cost > totalBalance) {
                JOptionPane.showMessageDialog(frame, "Not enough balance!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            totalBalance -= cost;

            boolean itemExists = false;
            for (InventoryItem item : inventory) {
                if (item.getDescription().equalsIgnoreCase(name)) {
                    item.setQuantity(item.getQuantity() + quantity);
                    itemExists = true;
                    break;
                }
            }

            if (!itemExists) {
                inventory.add(new InventoryItem(name, quantity, price));
            }

            updateInventoryTable();
            updateBalanceLabel();
            recordTransaction("Purchased " + quantity + " of " + name, -cost);
        });

        sellItemButton.addActionListener(e -> {
            String name = itemNameField.getText();
            int quantity;
            try {
                quantity = Integer.parseInt(itemQuantityField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid quantity!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (InventoryItem item : inventory) {
                if (item.getDescription().equalsIgnoreCase(name)) {
                    if (quantity > item.getQuantity()) {
                        JOptionPane.showMessageDialog(frame, "Not enough stock!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    double revenue = quantity * item.getSellingPrice();
                    totalBalance += revenue;
                    totalProfit += revenue - (quantity * item.getUnitPrice());
                    item.setQuantity(item.getQuantity() - quantity);
                    updateInventoryTable();
                    updateBalanceLabel();
                    updateProfitLabel();
                    recordTransaction("Sold " + quantity + " of " + name, revenue);
                    return;
                }
            }

            JOptionPane.showMessageDialog(frame, "Item not found!", "Error", JOptionPane.ERROR_MESSAGE);
        });

        tabbedPane.add("Inventory", inventoryPanel);

        // Transactions Tab
        JPanel transactionPanel = new JPanel(new BorderLayout());
        transactionModel = new DefaultTableModel(new String[]{"Date", "Description", "Amount (Rp)"}, 0);
        transactionTable = new JTable(transactionModel);
        transactionPanel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        JPanel transactionControls = new JPanel();
        JTextField transactionDescField = new JTextField(15);
        JTextField transactionAmountField = new JTextField(10);
        JButton addTransactionButton = new JButton("Add Transaction");

        transactionControls.add(new JLabel("Description:"));
        transactionControls.add(transactionDescField);
        transactionControls.add(new JLabel("Amount (Rp):"));
        transactionControls.add(transactionAmountField);
        transactionControls.add(addTransactionButton);
        transactionPanel.add(transactionControls, BorderLayout.SOUTH);

        addTransactionButton.addActionListener(e -> {
            String description = transactionDescField.getText();
            double amount;
            try {
                amount = Double.parseDouble(transactionAmountField.getText());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            totalBalance += amount;
            transactionModel.addRow(new Object[]{new Date(), description, amount});
            updateBalanceLabel();
            recordTransaction(description, amount);
        });

        tabbedPane.add("Transactions", transactionPanel);

        frame.add(tabbedPane, BorderLayout.CENTER);
        frame.setVisible(true);

        // Initial Modal
        String input = JOptionPane.showInputDialog(frame, "Enter initial capital (Rp):");
        try {
            totalBalance = Double.parseDouble(input);
        } catch (NumberFormatException ex) {
            totalBalance = 0;
        }
        updateBalanceLabel();
    }

    private void updateInventoryTable() {
        inventoryModel.setRowCount(0);
        for (InventoryItem item : inventory) {
            inventoryModel.addRow(new Object[]{item.getDescription(), item.getQuantity(), item.getUnitPrice(), item.getTotalValue()});
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText("Total Balance: Rp" + totalBalance);
    }

    private void updateProfitLabel() {
        profitLabel.setText("Total Profit: Rp" + totalProfit);
    }

    private void recordTransaction(String description, double amount) {
        transactions.add(new FinancialTransaction(description, amount));
        transactionModel.addRow(new Object[]{new Date(), description, amount});
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(InventoryFinanceManagerGUI::new);
    }
}
