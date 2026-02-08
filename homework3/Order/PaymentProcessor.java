package Order;
interface PaymentProcessor {
    void processPayment(Order order, String paymentDetails);
}