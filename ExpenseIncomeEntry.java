package expense_income_tracker;

public class ExpenseIncomeEntry {
    private final String date;
    private final String description;
    private final double amount;
    private final String bankname;
    private final String type; // The type of the entry (expense or income).
    
    
    public ExpenseIncomeEntry(String date, String description, double amount, String bankname, String type)
    {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.bankname= bankname;
        this.type = type;
    }
    
    public String getDate(){ return date;}
    public String getDescription(){ return description;}
    public double getAmount(){ return amount;}
    public String getBankName(){ return bankname;}
    public String getType(){ return type;}
}
