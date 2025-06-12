package expense_income_tracker;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.TitledBorder;

// The ExpensesIncomesTracker class extends JFrame to create the main application window.
public class ExpensesIncomesTracker extends JFrame {
    
    private final ExpenseIncomeTableModel tableModel;
    private final JTable table;
    private final JTextField dateField, descriptionField, amountField;
    private final JComboBox<String> typeCombobox;
    private final JTextField bankNameField;
    private final JButton addButton, resetButton;
    private final JLabel balanceLabel, incomeLabel, expenseLabel;
    private double balance, totalIncome, totalExpense;

    public ExpensesIncomesTracker() {
        
        balance = 0.0;
        totalIncome = 0.0;
        totalExpense = 0.0;

        tableModel = new ExpenseIncomeTableModel();
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new CustomCellRenderer());
        }

        // Input Fields
        dateField = new JTextField(10);
        dateField.setToolTipText("Enter date in DD/MM/YYYY format.");
        descriptionField = new JTextField(20);
        descriptionField.setToolTipText("Enter a short description of the transaction.");
        amountField = new JTextField(10);
        amountField.setToolTipText("Enter the transaction amount.");

        typeCombobox = new JComboBox<>(new String[]{"Expense", "Income"});
        typeCombobox.setToolTipText("Select Expense or Income");
        bankNameField = new JTextField(20);
        bankNameField.setToolTipText("Enter a short description of the transaction.");

        // Buttons
        addButton = new JButton("Add", new ImageIcon("icons/add.png"));
        addButton.setToolTipText("Click to add the transaction.");
        addButton.addActionListener(_ -> addEntry());

        resetButton = new JButton("Reset All", new ImageIcon("icons/reset.png"));
        resetButton.setToolTipText("Click to clear all entries and reset balance.");
        resetButton.addActionListener(_ -> resetAll());

        // Labels
        balanceLabel = new JLabel("Balance: $0.00");
        incomeLabel = new JLabel("Total Income: $0.00");
        expenseLabel = new JLabel("Total Expense: $0.00");

        // Panels
        JPanel inputPanel = new JPanel(new GridLayout(3, 4, 10, 10));
        inputPanel.setBorder(new TitledBorder("Add Transaction"));
        inputPanel.add(new JLabel("Date:"));
        inputPanel.add(dateField);
        inputPanel.add(new JLabel("Description:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Amount:"));
        inputPanel.add(amountField);
        inputPanel.add(new JLabel("Account name:"));
        inputPanel.add(bankNameField);
        inputPanel.add(new JLabel("Type:"));
        inputPanel.add(typeCombobox);
        
        inputPanel.add(addButton);
        inputPanel.add(resetButton);
         
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));

        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        summaryPanel.add(balanceLabel);
        summaryPanel.add(incomeLabel);
        summaryPanel.add(expenseLabel);

        setLayout(new BorderLayout(10, 10));
        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(summaryPanel, BorderLayout.SOUTH);

        setTitle("Expense & Income Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setVisible(true);

        // Export Button
        JPanel exportPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exportPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton exportButton = new JButton("Export to CSV");
        exportButton.setToolTipText("Export the records to a CSV file.");
        exportButton.addActionListener(_ -> exportToCSV());

// Add the export button to a new panel
        
        exportPanel.add(exportButton);

        bottomPanel.add(summaryPanel);
        bottomPanel.add(exportPanel);

// Add the bottom panel to the frame
        add(bottomPanel, BorderLayout.SOUTH);

}

    private void addEntry() {
        String date = dateField.getText().trim();
        String description = descriptionField.getText().trim();
        String amountStr = amountField.getText().trim();
        String type = (String) typeCombobox.getSelectedItem();
        String bankName = bankNameField.getText().trim();

        // Validate date
        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Invalid Date Format. Use DD/MM/YYYY.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Description cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be a positive value.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
        }} catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid Amount.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (type.equals("Expense")) {
            amount *= -1;
            totalExpense += Math.abs(amount);
        } else {
            totalIncome += amount;
        }

        ExpenseIncomeEntry entry = new ExpenseIncomeEntry(date, description, amount, bankName,  type);
        tableModel.addEntry(entry);
        balance += amount;

        updateLabels();
        clearInputFields();
    }

    private void resetAll() {
        tableModel.clearEntries();
        tableModel.fireTableDataChanged();
        balance = 0.0;
        totalIncome = 0.0;
        totalExpense = 0.0;
        updateLabels();
    }

    private void updateLabels() {
        balanceLabel.setText(String.format("Balance: $%.2f", balance));
        incomeLabel.setText(String.format("Total Income: $%.2f", totalIncome));
        expenseLabel.setText(String.format("Total Expense: $%.2f", totalExpense));
    }

    private void clearInputFields() {
        dateField.setText("");
        descriptionField.setText("");
        amountField.setText("");
        typeCombobox.setSelectedIndex(0);
        bankNameField.setText("");
    }

    private boolean isValidDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        format.setLenient(false);
        try {
            format.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
    private void exportToCSV() {
    JFileChooser fileChooser = new JFileChooser();
    fileChooser.setDialogTitle("Save CSV File");
    
    int userSelection = fileChooser.showSaveDialog(this);
    if (userSelection == JFileChooser.APPROVE_OPTION) {
        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        if (!filePath.toLowerCase().endsWith(".csv")) {
            filePath += ".csv";
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write the header
            writer.write("Date,Description,Amount,BankName,Type");
            writer.newLine();
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat csvFormat = new SimpleDateFormat("yyyy-MM-dd");
            
            // Write each entry
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String date = tableModel.getValueAt(i, 0).toString();
                try {
                    date = csvFormat.format(inputFormat.parse(date)); // Convert to ISO format
                } catch (ParseException e) {
                    date = "Invalid Date"; // Handle invalid date gracefully
                }
                String description = (String) tableModel.getValueAt(i, 1);
                String amount = (String) tableModel.getValueAt(i, 2);
                String bankName = (String) tableModel.getValueAt(i, 3);
                String type = (String) tableModel.getValueAt(i, 4);
                
                writer.write(String.join(",", date, description, amount, bankName, type));
                writer.newLine();
            }

            JOptionPane.showMessageDialog(this, "Records exported successfully to " + filePath, 
                                          "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error while exporting records: " + ex.getMessage(),
                                          "Export Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}

}