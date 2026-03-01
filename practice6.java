import java.util.*;

public class practice6 {

    //STRATEGY

    interface ICostCalculationStrategy {
        double calculate(double distance, int passengers, String serviceClass, boolean hasLuggage, double discountRate);
    }

    static class AirplaneStrategy implements ICostCalculationStrategy {
        public double calculate(double distance, int passengers, String serviceClass, boolean hasLuggage, double discountRate) {
            double base = distance * 0.12;
            if (serviceClass.equalsIgnoreCase("business")) base *= 2.2;
            if (hasLuggage) base += 30;
            base *= passengers;
            return base * (1 - discountRate);
        }
    }

    static class TrainStrategy implements ICostCalculationStrategy {
        public double calculate(double distance, int passengers, String serviceClass, boolean hasLuggage, double discountRate) {
            double base = distance * 0.05;
            if (serviceClass.equalsIgnoreCase("business")) base *= 1.8;
            if (hasLuggage) base += 10;
            base *= passengers;
            return base * (1 - discountRate);
        }
    }

    static class BusStrategy implements ICostCalculationStrategy {
        public double calculate(double distance, int passengers, String serviceClass, boolean hasLuggage, double discountRate) {
            double base = distance * 0.02;
            if (hasLuggage) base += 5;
            base *= passengers;
            return base * (1 - discountRate);
        }
    }

    static class CarRentalStrategy implements ICostCalculationStrategy {
        public double calculate(double distance, int passengers, String serviceClass, boolean hasLuggage, double discountRate) {
            double base = distance * 0.08 + 50;
            if (serviceClass.equalsIgnoreCase("business")) base += 100;
            return base * (1 - discountRate);
        }
    }

    static class TravelBookingContext {
        private ICostCalculationStrategy strategy;
        private String transportName;

        public void setStrategy(ICostCalculationStrategy strategy, String transportName) {
            this.strategy = strategy;
            this.transportName = transportName;
        }

        public void book(double distance, int passengers, String serviceClass, boolean hasLuggage, double discountRate) {
            if (strategy == null) {
                System.out.println("[Ошибка] Стратегия не выбрана.");
                return;
            }
            if (distance <= 0 || passengers <= 0) {
                System.out.println("[Ошибка] Расстояние и количество пассажиров должны быть больше 0.");
                return;
            }
            double cost = strategy.calculate(distance, passengers, serviceClass, hasLuggage, discountRate);
            System.out.printf("Транспорт: %s | Расстояние: %.0f км | Класс: %s | Пассажиры: %d | Багаж: %s | Скидка: %.0f%%%n",
                    transportName, distance, serviceClass, passengers, hasLuggage ? "да" : "нет", discountRate * 100);
            System.out.printf("Итоговая стоимость: %.2f USD%n%n", cost);
        }
    }

    //OBSERVER

    interface IStockObserver {
        void onUpdate(String ticker, double newPrice, double oldPrice);
        String getName();
        Set<String> getSubscriptions();
    }

    interface IStockSubject {
        void subscribe(IStockObserver observer, String... tickers);
        void unsubscribe(IStockObserver observer, String ticker);
        void setPrice(String ticker, double price);
        void printReport();
    }

    static class StockExchange implements IStockSubject {
        private Map<String, Double> prices = new HashMap<>();
        private Map<String, List<IStockObserver>> subscriptions = new HashMap<>();
        private List<String> log = new ArrayList<>();
        private Map<String, Integer> notificationCount = new HashMap<>();

        public void subscribe(IStockObserver observer, String... tickers) {
            for (String ticker : tickers) {
                subscriptions.computeIfAbsent(ticker, k -> new ArrayList<>()).add(observer);
                observer.getSubscriptions().add(ticker);
                log.add("[LOG] " + observer.getName() + " подписался на " + ticker);
            }
        }

        public void unsubscribe(IStockObserver observer, String ticker) {
            List<IStockObserver> list = subscriptions.get(ticker);
            if (list != null) {
                list.remove(observer);
                observer.getSubscriptions().remove(ticker);
                log.add("[LOG] " + observer.getName() + " отписался от " + ticker);
            }
        }

        public void setPrice(String ticker, double newPrice) {
            double oldPrice = prices.getOrDefault(ticker, 0.0);
            prices.put(ticker, newPrice);
            log.add(String.format("[LOG] Цена %s изменилась: %.2f -> %.2f", ticker, oldPrice, newPrice));
            List<IStockObserver> observers = subscriptions.getOrDefault(ticker, Collections.emptyList());
            for (IStockObserver observer : new ArrayList<>(observers)) {
                observer.onUpdate(ticker, newPrice, oldPrice);
                notificationCount.merge(observer.getName(), 1, Integer::sum);
            }
        }

        public void printReport() {
            System.out.println("\n=== ОТЧЕТ ПО ПОДПИСЧИКАМ ===");
            for (Map.Entry<String, List<IStockObserver>> entry : subscriptions.entrySet()) {
                System.out.print("Акция " + entry.getKey() + ": ");
                entry.getValue().forEach(o -> System.out.print(o.getName() + " "));
                System.out.println();
            }
            System.out.println("\nКоличество уведомлений:");
            notificationCount.forEach((name, count) -> System.out.println("  " + name + ": " + count));
            System.out.println("\nЖурнал событий:");
            log.forEach(System.out::println);
        }
    }

    static class TraderObserver implements IStockObserver {
        private String name;
        private Set<String> subscriptions = new HashSet<>();
        private double priceThreshold;

        TraderObserver(String name, double priceThreshold) {
            this.name = name;
            this.priceThreshold = priceThreshold;
        }

        public void onUpdate(String ticker, double newPrice, double oldPrice) {
            if (newPrice >= priceThreshold) {
                System.out.printf("[Трейдер %s] %s: %.2f (было %.2f) — ВНИМАНИЕ: цена выше порога %.2f!%n",
                        name, ticker, newPrice, oldPrice, priceThreshold);
            } else {
                System.out.printf("[Трейдер %s] %s: %.2f (было %.2f)%n", name, ticker, newPrice, oldPrice);
            }
        }

        public String getName() { return "Трейдер_" + name; }
        public Set<String> getSubscriptions() { return subscriptions; }
    }

    static class TradingRobotObserver implements IStockObserver {
        private String name;
        private Set<String> subscriptions = new HashSet<>();
        private double buyThreshold;
        private double sellThreshold;

        TradingRobotObserver(String name, double buyThreshold, double sellThreshold) {
            this.name = name;
            this.buyThreshold = buyThreshold;
            this.sellThreshold = sellThreshold;
        }

        public void onUpdate(String ticker, double newPrice, double oldPrice) {
            if (newPrice <= buyThreshold) {
                System.out.printf("[Робот %s] %s = %.2f — ПОКУПАЮ (порог покупки: %.2f)%n", name, ticker, newPrice, buyThreshold);
            } else if (newPrice >= sellThreshold) {
                System.out.printf("[Робот %s] %s = %.2f — ПРОДАЮ (порог продажи: %.2f)%n", name, ticker, newPrice, sellThreshold);
            } else {
                System.out.printf("[Робот %s] %s = %.2f — наблюдаю%n", name, ticker, newPrice);
            }
        }

        public String getName() { return "Робот_" + name; }
        public Set<String> getSubscriptions() { return subscriptions; }
    }

    static class EmailNotifierObserver implements IStockObserver {
        private String email;
        private Set<String> subscriptions = new HashSet<>();

        EmailNotifierObserver(String email) { this.email = email; }

        public void onUpdate(String ticker, double newPrice, double oldPrice) {
            System.out.printf("[Email -> %s] Уведомление: акция %s изменила цену с %.2f до %.2f%n",
                    email, ticker, oldPrice, newPrice);
        }

        public String getName() { return "Email_" + email; }
        public Set<String> getSubscriptions() { return subscriptions; }
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== СИСТЕМА БРОНИРОВАНИЯ ПУТЕШЕСТВИЙ (Паттерн Стратегия) ===\n");
        TravelBookingContext booking = new TravelBookingContext();

        System.out.println("Выберите транспорт: 1 - Самолёт, 2 - Поезд, 3 - Автобус, 4 - Аренда авто");
        int transport = scanner.nextInt();

        System.out.print("Расстояние (км): ");
        double distance = scanner.nextDouble();

        System.out.print("Количество пассажиров: ");
        int passengers = scanner.nextInt();

        System.out.print("Класс обслуживания (economy/business): ");
        String serviceClass = scanner.next();

        System.out.print("Багаж? (true/false): ");
        boolean luggage = scanner.nextBoolean();

        System.out.println("Тип скидки: 1 - Нет (0%), 2 - Детская (15%), 3 - Пенсионная (20%), 4 - Групповая (10%)");
        int discountChoice = scanner.nextInt();
        double discount = switch (discountChoice) {
            case 2 -> 0.15;
            case 3 -> 0.20;
            case 4 -> 0.10;
            default -> 0.0;
        };

        switch (transport) {
            case 1 -> booking.setStrategy(new AirplaneStrategy(), "Самолёт");
            case 2 -> booking.setStrategy(new TrainStrategy(), "Поезд");
            case 3 -> booking.setStrategy(new BusStrategy(), "Автобус");
            case 4 -> booking.setStrategy(new CarRentalStrategy(), "Аренда авто");
            default -> { System.out.println("Неверный выбор транспорта."); return; }
        }

        booking.book(distance, passengers, serviceClass, luggage, discount);

        System.out.println("Пример смены стратегии — пересадка (поезд + автобус):");
        TravelBookingContext leg1 = new TravelBookingContext();
        leg1.setStrategy(new TrainStrategy(), "Поезд");
        leg1.book(distance * 0.6, passengers, "economy", false, discount);

        TravelBookingContext leg2 = new TravelBookingContext();
        leg2.setStrategy(new BusStrategy(), "Автобус");
        leg2.book(distance * 0.4, passengers, "economy", luggage, discount);

        System.out.println("=== БИРЖЕВЫЕ ТОРГИ (Паттерн Наблюдатель) ===\n");
        StockExchange exchange = new StockExchange();

        TraderObserver alice = new TraderObserver("Алиса", 200.0);
        TraderObserver bob = new TraderObserver("Боб", 150.0);
        TradingRobotObserver robot = new TradingRobotObserver("AlphaBot", 130.0, 190.0);
        EmailNotifierObserver emailNotifier = new EmailNotifierObserver("admin@stock.kz");

        exchange.subscribe(alice, "AAPL", "TSLA");
        exchange.subscribe(bob, "AAPL");
        exchange.subscribe(robot, "TSLA", "GOOGL");
        exchange.subscribe(emailNotifier, "AAPL", "TSLA", "GOOGL");

        System.out.println("-- Изменение цен --");
        exchange.setPrice("AAPL", 175.50);
        exchange.setPrice("TSLA", 195.00);
        exchange.setPrice("GOOGL", 125.00);

        System.out.println("\n-- Боб отписывается от AAPL --");
        exchange.unsubscribe(bob, "AAPL");
        exchange.setPrice("AAPL", 210.00);

        exchange.printReport();
        scanner.close();
    }
}
