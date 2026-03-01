import java.util.*;

public class homework6 {

    //STRATEGY

    interface IPaymentStrategy {
        void pay(double amount);
    }

    static class CreditCardPayment implements IPaymentStrategy {
        private String cardNumber;
        CreditCardPayment(String cardNumber) { this.cardNumber = cardNumber; }
        public void pay(double amount) {
            System.out.printf("Оплата банковской картой [%s]: %.2f USD%n", cardNumber, amount);
        }
    }

    static class PayPalPayment implements IPaymentStrategy {
        private String email;
        PayPalPayment(String email) { this.email = email; }
        public void pay(double amount) {
            System.out.printf("Оплата через PayPal [%s]: %.2f USD%n", email, amount);
        }
    }

    static class CryptoPayment implements IPaymentStrategy {
        private String walletAddress;
        CryptoPayment(String walletAddress) { this.walletAddress = walletAddress; }
        public void pay(double amount) {
            System.out.printf("Оплата криптовалютой [%s]: %.2f USD%n", walletAddress, amount);
        }
    }

    static class PaymentContext {
        private IPaymentStrategy strategy;
        public void setStrategy(IPaymentStrategy strategy) { this.strategy = strategy; }
        public void executePayment(double amount) {
            if (strategy == null) {
                System.out.println("Стратегия оплаты не выбрана.");
                return;
            }
            strategy.pay(amount);
        }
    }

    //OBSERVER

    interface IObserver {
        void update(String currency, double rate);
    }

    interface ISubject {
        void addObserver(IObserver observer);
        void removeObserver(IObserver observer);
        void notifyObservers();
    }

    static class CurrencyExchange implements ISubject {
        private List<IObserver> observers = new ArrayList<>();
        private Map<String, Double> rates = new HashMap<>();

        public void addObserver(IObserver observer) { observers.add(observer); }
        public void removeObserver(IObserver observer) { observers.remove(observer); }

        public void setRate(String currency, double rate) {
            rates.put(currency, rate);
            notifyObservers();
        }

        public void notifyObservers() {
            for (Map.Entry<String, Double> entry : rates.entrySet()) {
                for (IObserver observer : observers) {
                    observer.update(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    static class BankObserver implements IObserver {
        private String bankName;
        BankObserver(String bankName) { this.bankName = bankName; }
        public void update(String currency, double rate) {
            System.out.printf("[Банк %s] Новый курс %s: %.2f KZT%n", bankName, currency, rate);
        }
    }

    static class TraderObserver implements IObserver {
        private String traderName;
        TraderObserver(String traderName) { this.traderName = traderName; }
        public void update(String currency, double rate) {
            System.out.printf("[Трейдер %s] Курс %s изменился: %.2f — анализирую рынок...%n", traderName, currency, rate);
        }
    }

    static class MobileAppObserver implements IObserver {
        public void update(String currency, double rate) {
            System.out.printf("[Мобильное приложение] Уведомление: курс %s = %.2f KZT%n", currency, rate);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== ПАТТЕРН СТРАТЕГИЯ: Оплата ===");
        PaymentContext context = new PaymentContext();

        System.out.println("Выберите способ оплаты: 1 - Карта, 2 - PayPal, 3 - Крипто");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1 -> context.setStrategy(new CreditCardPayment("4111-1111-1111-1111"));
            case 2 -> context.setStrategy(new PayPalPayment("user@example.com"));
            case 3 -> context.setStrategy(new CryptoPayment("0xABCDEF123456"));
            default -> System.out.println("Неверный выбор.");
        }
        context.executePayment(150.00);

        System.out.println("\nСмена стратегии на PayPal:");
        context.setStrategy(new PayPalPayment("admin@shop.com"));
        context.executePayment(75.50);

        System.out.println("\n=== ПАТТЕРН НАБЛЮДАТЕЛЬ: Курсы валют ===");
        CurrencyExchange exchange = new CurrencyExchange();

        BankObserver halyk = new BankObserver("Halyk");
        BankObserver kaspi = new BankObserver("Kaspi");
        TraderObserver trader = new TraderObserver("Алибек");
        MobileAppObserver app = new MobileAppObserver();

        exchange.addObserver(halyk);
        exchange.addObserver(kaspi);
        exchange.addObserver(trader);
        exchange.addObserver(app);

        System.out.println("\n-- Обновление курса USD --");
        exchange.setRate("USD", 498.50);

        System.out.println("\n-- Удаление Kaspi, обновление EUR --");
        exchange.removeObserver(kaspi);
        exchange.setRate("EUR", 541.30);

        scanner.close();
    }
}
