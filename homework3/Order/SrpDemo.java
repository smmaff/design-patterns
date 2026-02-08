package Order;
public class SrpDemo {
    public static void main(String[] args) {
        Order order = new Order("Laptop", 2, 500.0);

        OrderService service = new OrderService(
                new OrderPriceCalculator(),
                new ConsolePaymentProcessor(),
                new EmailNotifier()
        );

        service.placeOrder(order, "VISA **** 1234", "user@example.com");
    }
}