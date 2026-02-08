package Order;
class onsolePaymentProcessor implements PaymentProcessor {
    @Override
    public void processPayment(Order order, String paymentDetails) {
        System.out.println("Payment processed using: " + paymentDetails);
    }
}
