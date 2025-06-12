package expense_income_tracker;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;

public class CustomCellRenderer extends DefaultTableCellRenderer {
    //private static final Color DARK_GREEN = new Color(0, 100, 0); // RGB values for dark green

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, 
                                                   boolean hasFocus, int row, int column) {
        Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // Get the type column value for the current row
        String type = (String) table.getModel().getValueAt(row, 4); // "Type" is at column index 4

        if (type.equalsIgnoreCase("Expense")) {
            cell.setBackground(Color.RED);
        } else if (type.equalsIgnoreCase("Income")) {
            cell.setBackground(Color.GREEN);
        } else {
            cell.setBackground(Color.BLACK); // Default text color
        }

        return cell;
    }
}

