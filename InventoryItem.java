// Subclass for inventory items
class InventoryItem extends Record {
    private int quantity;
    private double unitPrice;

    public InventoryItem(String description, int quantity, double unitPrice) {
        super(description);
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalValue() {
        return quantity * unitPrice;
    }

    public double getSellingPrice() {
        return unitPrice * 1.1; // 10% markup
    }

    @Override
    public String toString() {
        return "Item: " + description + ", Quantity: " + quantity + ", Unit Price: Rp" + unitPrice + ", Total Value: Rp" + getTotalValue();
    }
}
