package Order;
class OrderService {
    private final OrderPriceCalculator calculator;
    private final PaymentProcessor paymentProcessor;
    private final Notifier notifier;

    public OrderService(OrderPriceCalculator calculator, PaymentProcessor paymentProcessor, Notifier notifier) {
        this.calculator = calculator;
        this.paymentProcessor = paymentProcessor;
        this.notifier = notifier;
    }

    public void placeOrder(Order order, String paymentDetails, String email) {
        double total = calculator.calculateTotal(order);
        paymentProcessor.processPayment(order, paymentDetails);
        notifier.sendConfirmation(email, "Order total: " + total);
    }
}