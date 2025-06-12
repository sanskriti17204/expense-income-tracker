package expense_income_tracker;

import javax.swing.SwingUtilities;

public class Expense_Income_Tracker {
public static void main(String[] args) {
        
        SwingUtilities.invokeLater(ExpensesIncomesTracker::new);
        
    }
}
