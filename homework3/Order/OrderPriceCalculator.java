package Order;
class OrderPriceCalculator {
    public double calculateTotal(Order order) {
        double subtotal = order.getQuantity() * order.getPricePerUnit();
        double discountMultiplier = 0.9;
        return subtotal * discountMultiplier;
    }
}
