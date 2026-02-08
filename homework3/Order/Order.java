package Order;
class Order {
    private final String productName;
    private final int quantity;
    private final double pricePerUnit;
    public Order(String productName, int quantity, double pricePerUnit) {
        this.productName = productName;
        this.quantity = quantity;
        this.pricePerUnit = pricePerUnit;
    }
    public String getProductName() { return productName; }
    public int getQuantity() { return quantity; }
    public double getPricePerUnit() { return pricePerUnit; }
}
