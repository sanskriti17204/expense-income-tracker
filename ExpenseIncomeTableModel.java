package expense_income_tracker;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ExpenseIncomeTableModel extends AbstractTableModel {

    // List to store the entries (rows) in the table
    private final List<ExpenseIncomeEntry> entries;
    // Column names for the table
    private final String[] columnNames = {"Date","Description","Amount", "BankName", "Type"};
    
    /**
     * Constructor to initialize the table model.
     */
    public ExpenseIncomeTableModel(){
        entries = new ArrayList<>();
    }
            

    /**
     * Add a new entry to the table.
     *
     * @param entry The expense or income entry to add.
     */
    public void addEntry(ExpenseIncomeEntry entry){
        entries.add(entry);
        // Notify the table that a new row has been inserted
        fireTableRowsInserted(entries.size()-1, entries.size()-1);
    }

    public void clearEntries() {
        int size = entries.size();
        if (size > 0) {
            entries.clear();
            fireTableRowsDeleted(0, size - 1); // Notify the table that rows have been removed
        }
    }  
    
    @Override
    public int getRowCount() { return entries.size(); }

    @Override
    public int getColumnCount() { return columnNames.length;}

    @Override
    public String getColumnName(int column){ return columnNames[column]; }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        
        ExpenseIncomeEntry entry = entries.get(rowIndex);
        
        // Return the value for the cell based on the column index
        return switch(columnIndex){
            case 0 -> entry.getDate();
            case 1-> entry.getDescription();
            case 2-> String.format("₹%.2f", Math.abs(entry.getAmount()));
            case 3->entry.getBankName();
            case 4-> entry.getType();
            default-> null;
        };
        
        
    }

}

