// Subclass for financial transactions
class FinancialTransaction extends Record {
    private double amount;

    public FinancialTransaction(String description, double amount) {
        super(description);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Date: " + date + ", Description: " + description + ", Amount: Rp" + amount;
    }
}
