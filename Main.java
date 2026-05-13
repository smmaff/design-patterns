import java.util.List;

interface IDeliveryMethod {
    double calculateCost(double distance);
    String trackStatus();
    void processDelivery(Order order);
}
interface IPaymentMethod {
    boolean processTransaction(double amount);
    boolean refund(double amount);
    boolean confirmPayment();
}
interface IOrderFactory {
    IDeliveryMethod createDelivery();
    IPaymentMethod createPayment();
}
class CourierDelivery implements IDeliveryMethod {
    private final double ratePerKm;
    private final String trackingNumber;
    public CourierDelivery(double ratePerKm) {
        this.ratePerKm = ratePerKm;
        this.trackingNumber = "TRK-" + (int)(Math.random() * 100000);
    }
    public double calculateCost(double distance) { return distance * ratePerKm; }
    public String trackStatus() { return "Курьер в пути, отслеживание курьера: " + trackingNumber; }
    public void processDelivery(Order order) {
        System.out.println("Курьер назначен для заказа #" + order.getId()
                + "Стоимость: " + calculateCost(order.getDistance()) + " тг");
        System.out.println(trackStatus());
    }
}
class PickupDelivery implements IDeliveryMethod {
    private final List<String> pickupPoints;
    private boolean readyForPickup;
    public PickupDelivery(List<String> pickupPoints) {
        this.pickupPoints = pickupPoints;
        this.readyForPickup = false;
    }
    public double calculateCost(double distance) { return 0; }
    public String trackStatus() {
        return "Статус: " + (readyForPickup ? "Готов к выдаче" : "Готовится")
                + "Пункты: " + pickupPoints;
    }
    public void processDelivery(Order order) {
        readyForPickup = true;
        System.out.println("Заказ #" + order.getId() + " готов к самовывозу.");
        System.out.println("Ближайший пункт: " + pickupPoints.get(0));
        System.out.println(trackStatus());
    }
}
class PostalDelivery implements IDeliveryMethod {
    private final String region;
    private final int daysEstimate;
    public PostalDelivery(String region, int daysEstimate) {
        this.region = region;
        this.daysEstimate = daysEstimate;
    }
    public double calculateCost(double distance) { return region.equals("local") ? 500 : 1500; }

    public String trackStatus() { return "CDEK | Регион: " + region + " | Срок: " + daysEstimate + " дней"; }
    public void processDelivery(Order order) {
        System.out.println(" Заказ #" + order.getId() + " отправлен CDEK.");
        System.out.println(" Стоимость: " + calculateCost(order.getDistance()) + " тг | " + trackStatus());
    }
}
class CardPayment implements IPaymentMethod {
    private final String cardNumber;
    private boolean authorized;
    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
        this.authorized = false;
    }
    private boolean validate() { return cardNumber != null && cardNumber.length() == 16; }
    public boolean processTransaction(double amount) {
        if (!validate()) { System.out.println("Ошибка: неверный номер карты"); return false; }
        authorized = true;
        System.out.println("Карта авторизована. Транзакция: " + amount + " тг выполнена.");
        return true;
    }
    public boolean refund(double amount) {
        System.out.println("Возврат " + amount + " тг на карту *" + cardNumber.substring(12));
        return true;
    }
    public boolean confirmPayment() {
        System.out.println("Платёж картой подтверждён: " + authorized);
        return authorized;
    }
}
class CashPayment implements IPaymentMethod {
    private boolean paidOnDelivery;
    public boolean processTransaction(double amount) {
        System.out.println("Оплата наличными " + amount + " тг — ожидается при доставке.");
        paidOnDelivery = false;
        return true;
    }
    public boolean refund(double amount) {
        System.out.println("Возврат наличными: " + amount + " тг");
        return true;
    }
    public boolean confirmPayment() {
        paidOnDelivery = true;
        System.out.println("Оплата наличными подтверждена при доставке.");
        return true;
    }
}
class OnlinePayment implements IPaymentMethod {
    private final String serviceProvider;
    private String status;

    public OnlinePayment(String serviceProvider) {
        this.serviceProvider = serviceProvider;
        this.status = "PENDING";
    }
    public boolean processTransaction(double amount) {
        System.out.println("[" + serviceProvider + "] Авторизация на " + amount + " тг...");
        status = "AUTHORIZED";
        notifyStatus();
        return true;
    }
    public boolean refund(double amount) {
        status = "REFUNDED";
        System.out.println("[" + serviceProvider + "] Возврат " + amount + " тг инициирован.");
        notifyStatus();
        return true;
    }
    public boolean confirmPayment() {
        status = "CONFIRMED";
        notifyStatus();
        return true;
    }
    private void notifyStatus() {
        System.out.println("[" + serviceProvider + "] Уведомление: статус — " + status);
    }
}
class ExpressOrderFactory implements IOrderFactory {
    public IDeliveryMethod createDelivery() { return new CourierDelivery(300); }
    public IPaymentMethod createPayment() { return new CardPayment("4400543223321235"); }
}
class StandardOrderFactory implements IOrderFactory {
    public IDeliveryMethod createDelivery() { return new PostalDelivery("Almaty region", 7); }
    public IPaymentMethod createPayment() { return new OnlinePayment("Apple Pay"); }
}
class LocalPickupOrderFactory implements IOrderFactory {
    public IDeliveryMethod createDelivery() {
        return new PickupDelivery(List.of("Алматы, ул. Байтурсынова 125", "Алматы, ул. Шашкина 40"));
    }
    public IPaymentMethod createPayment() { return new CashPayment(); }
}
//заказик
class Order {
    private final int id;
    private final String product;
    private final double amount;
    private final double distance;
    private IDeliveryMethod deliveryMethod;
    private IPaymentMethod paymentMethod;
    public Order(int id, String product, double amount, double distance) {
        this.id = id;
        this.product = product;
        this.amount = amount;
        this.distance = distance;
    }
    public void configure(IOrderFactory factory) {
        this.deliveryMethod = factory.createDelivery();
        this.paymentMethod = factory.createPayment();
    }
    public void process() {
        System.out.println("\n=== Заказ #" + id + " [" + product + "] ===");
        deliveryMethod.processDelivery(this);
        paymentMethod.processTransaction(amount);
        paymentMethod.confirmPayment();
        System.out.println("Заказ #" + id + " успешно оформлен.");
    }
    public int getId() { return id; }
    public double getDistance() { return distance; }
    public double getAmount() { return amount; }
}
public class Main {
    public static void main(String[] args) {
        Order order1 = new Order(1001, "Айкос", 350000, 15.0);
        order1.configure(new ExpressOrderFactory());
        order1.process();

        Order order2 = new Order(1002, "Магнитик из Таиланда", 8500, 0);
        order2.configure(new LocalPickupOrderFactory());
        order2.process();

        Order order3 = new Order(1003, "Телефон", 180000, 200.0);
        order3.configure(new StandardOrderFactory());
        order3.process();
    }
}



























