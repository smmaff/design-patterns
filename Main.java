import java.util.*;
final class Product {
    private final String name;
    private final double unitPrice;
    public Product(String name, double unitPrice) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("name");
        if (unitPrice < 0) throw new IllegalArgumentException("unitPrice");
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public String name() { return name; }
    public double unitPrice() { return unitPrice; }
}
final class OrderItem {
    private final Product product;
    private final int quantity;

    public OrderItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("product");
        if (quantity <= 0) throw new IllegalArgumentException("quantity");
        this.product = product;
        this.quantity = quantity;
    }

    public Product product() { return product; }
    public int quantity() { return quantity; }
    public double lineTotal() { return product.unitPrice() * quantity; }
}
enum OrderStatus {
    CREATED, PAID, SHIPPED, DELIVERED, CANCELED
}
final class Order {
    private final List<OrderItem> items = new ArrayList<>();
    private Payment payment;
    private Delivery delivery;
    private OrderStatus status = OrderStatus.CREATED;

    public void addItem(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }
    public List<OrderItem> items() {
        return Collections.unmodifiableList(items);
    }
    public void setPayment(Payment payment) { this.payment = payment; }
    public void setDelivery(Delivery delivery) { this.delivery = delivery; }
    public Payment payment() {
        if (payment == null) throw new IllegalStateException("Payment not set");
        return payment;
    }
    public Delivery delivery() {
        if (delivery == null) throw new IllegalStateException("Delivery not set");
        return delivery;
    }
    public OrderStatus status() { return status; }
    public void setStatus(OrderStatus status) { this.status = status; }

    public double subtotal() {
        return items.stream().mapToDouble(OrderItem::lineTotal).sum();
    }
    public double totalWithDiscounts(DiscountCalculator discountCalculator) {
        double base = subtotal();
        double discount = discountCalculator.totalDiscount(this, base);
        double result = base - discount;
        return Math.max(0, result);
    }
}
interface Payment {
    PaymentResult processPayment(double amount);
}
record PaymentResult(boolean success, String message) {}
final class CreditCardPayment implements Payment {
    private final String maskedCard;
    public CreditCardPayment(String maskedCard) {
        this.maskedCard = maskedCard;
    }
    @Override
    public PaymentResult processPayment(double amount) {
        return new PaymentResult(true, "Credit card charged (" + maskedCard + ") amount=" + amount);
    }
}
final class PayPalPayment implements Payment {
    private final String accountEmail;
    public PayPalPayment(String accountEmail) {
        this.accountEmail = accountEmail;
    }
    @Override
    public PaymentResult processPayment(double amount) {
        return new PaymentResult(true, "PayPal payment from " + accountEmail + " amount=" + amount);
    }
}
final class BankTransferPayment implements Payment {
    private final String iban;
    public BankTransferPayment(String iban) {
        this.iban = iban;
    }
    @Override
    public PaymentResult processPayment(double amount) {
        return new PaymentResult(true, "Bank transfer to " + iban + " amount=" + amount);
    }
}
interface Delivery {
    DeliveryResult deliverOrder(Order order);
}
record DeliveryResult(boolean accepted, String message) {}
final class CourierDelivery implements Delivery {
    private final String address;
    public CourierDelivery(String address) {
        this.address = address;
    }
    @Override
    public DeliveryResult deliverOrder(Order order) {
        return new DeliveryResult(true, "Courier delivery to: " + address);
    }
}
final class PostDelivery implements Delivery {
    private final String address;
    public PostDelivery(String address) {
        this.address = address;
    }
    @Override
    public DeliveryResult deliverOrder(Order order) {
        return new DeliveryResult(true, "Post delivery to: " + address);
    }
}
final class PickUpPointDelivery implements Delivery {
    private final String pointId;
    public PickUpPointDelivery(String pointId) {
        this.pointId = pointId;
    }
    @Override
    public DeliveryResult deliverOrder(Order order) {
        return new DeliveryResult(true, "Pickup point delivery, pointId=" + pointId);
    }
}
interface Notification {
    void sendNotification(String message);
}
final class EmailNotification implements Notification {
    private final String email;
    public EmailNotification(String email) {
        this.email = email;
    }
    @Override
    public void sendNotification(String message) {
        System.out.println("[EMAIL to " + email + "] " + message);
    }
}
final class SmsNotification implements Notification {
    private final String phone;
    public SmsNotification(String phone) {
        this.phone = phone;
    }
    @Override
    public void sendNotification(String message) {
        System.out.println("[SMS to " + phone + "] " + message);
    }
}
interface DiscountRule {
    double discountAmount(Order order, double baseAmount);
}
final class DiscountCalculator {
    private final List<DiscountRule> rules;
    public DiscountCalculator(List<DiscountRule> rules) {
        this.rules = new ArrayList<>(rules);
    }
    public double totalDiscount(Order order, double baseAmount) {
        double total = 0.0;
        for (DiscountRule rule : rules) {
            total += Math.max(0, rule.discountAmount(order, baseAmount));
        }
        return Math.min(total, baseAmount);
    }
}
final class PercentageOverThresholdDiscount implements DiscountRule {
    private final double threshold;
    private final double percent; // 0.10 = 10%

    public PercentageOverThresholdDiscount(double threshold, double percent) {
        this.threshold = threshold;
        this.percent = percent;
    }
    @Override
    public double discountAmount(Order order, double baseAmount) {
        return baseAmount >= threshold ? baseAmount * percent : 0.0;
    }
}
final class FixedDiscountIfTotalQuantityAtLeast implements DiscountRule {
    private final int minQuantity;
    private final double fixedDiscount;

    public FixedDiscountIfTotalQuantityAtLeast(int minQuantity, double fixedDiscount) {
        this.minQuantity = minQuantity;
        this.fixedDiscount = fixedDiscount;
    }
    @Override
    public double discountAmount(Order order, double baseAmount) {
        int qty = order.items().stream().mapToInt(OrderItem::quantity).sum();
        return qty >= minQuantity ? fixedDiscount : 0.0;
    }
}
final class OrderService {
    private final DiscountCalculator discountCalculator;
    private final Notification notification;

    public OrderService(DiscountCalculator discountCalculator, Notification notification) {
        this.discountCalculator = discountCalculator;
        this.notification = notification;
    }
    public double calculateTotal(Order order) {
        return order.totalWithDiscounts(discountCalculator);
    }
    public void pay(Order order) {
        double amount = calculateTotal(order);
        PaymentResult result = order.payment().processPayment(amount);

        if (result.success()) {
            order.setStatus(OrderStatus.PAID);
            notification.sendNotification("Order paid. " + result.message());
        } else {
            order.setStatus(OrderStatus.CANCELED);
            notification.sendNotification("Payment failed. " + result.message());
        }
    }
    public void ship(Order order) {
        DeliveryResult result = order.delivery().deliverOrder(order);
        if (result.accepted()) {
            order.setStatus(OrderStatus.SHIPPED);
            notification.sendNotification("Order shipped. " + result.message());
        } else {
            notification.sendNotification("Delivery rejected. " + result.message());
        }
    }
}
//Demo usage
public class Main {
    public static void main(String[] args) {
        Order order = new Order();
        order.addItem(new Product("Keyboard", 15000), 1);
        order.addItem(new Product("Mouse", 7000), 2);

        order.setPayment(new PayPalPayment("buyer@example.com"));
        order.setDelivery(new CourierDelivery("Almaty, Abay ave, 10"));

        DiscountCalculator discounts = new DiscountCalculator(List.of(
                new PercentageOverThresholdDiscount(20000, 0.10),
                new FixedDiscountIfTotalQuantityAtLeast(3, 1000)
        ));

        Notification notification = new EmailNotification("buyer@example.com");

        OrderService service = new OrderService(discounts, notification);

        System.out.println("Subtotal = " + order.subtotal());
        System.out.println("Total(with discounts) = " + service.calculateTotal(order));

        service.pay(order);
        service.ship(order);

        notification.sendNotification("Current status: " + order.status());
    }
}
