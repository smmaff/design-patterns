package practice9;

import java.util.ArrayList;
import java.util.List;

public class Main {
    static class RoomBookingSystem {
        public void checkAvailability(String roomType) {
            System.out.println("Проверка доступности номера типа: " + roomType);
        }

        public void bookRoom(String guestName, String roomType) {
            System.out.println("Номер типа " + roomType + " забронирован для " + guestName);
        }

        public void cancelBooking(String guestName) {
            System.out.println("Бронирование для " + guestName + " отменено");
        }
    }

    static class RestaurantSystem {
        public void bookTable(String guestName, int persons) {
            System.out.println("Столик на " + persons + " человек забронирован для " + guestName);
        }

        public void orderFood(String guestName, String food) {
            System.out.println("Для " + guestName + " заказана еда: " + food);
        }
    }

    static class EventManagementSystem {
        public void bookHall(String eventName) {
            System.out.println("Конференц-зал забронирован для мероприятия: " + eventName);
        }

        public void orderEquipment(String equipment) {
            System.out.println("Оборудование заказано: " + equipment);
        }
    }

    static class CleaningService {
        public void scheduleCleaning(String roomType) {
            System.out.println("Уборка запланирована для номера типа: " + roomType);
        }

        public void cleanNow(String roomType) {
            System.out.println("Срочная уборка выполнена для номера типа: " + roomType);
        }
    }

    static class TaxiService {
        public void callTaxi(String guestName) {
            System.out.println("Такси вызвано для " + guestName);
        }
    }

    static class HotelFacade {
        private RoomBookingSystem roomBookingSystem;
        private RestaurantSystem restaurantSystem;
        private EventManagementSystem eventManagementSystem;
        private CleaningService cleaningService;
        private TaxiService taxiService;

        public HotelFacade(RoomBookingSystem roomBookingSystem, RestaurantSystem restaurantSystem,
                           EventManagementSystem eventManagementSystem, CleaningService cleaningService,
                           TaxiService taxiService) {
            this.roomBookingSystem = roomBookingSystem;
            this.restaurantSystem = restaurantSystem;
            this.eventManagementSystem = eventManagementSystem;
            this.cleaningService = cleaningService;
            this.taxiService = taxiService;
        }

        public void bookRoomWithServices(String guestName, String roomType, String food) {
            System.out.println("\nСценарий: бронирование номера с едой и уборкой");
            roomBookingSystem.checkAvailability(roomType);
            roomBookingSystem.bookRoom(guestName, roomType);
            restaurantSystem.orderFood(guestName, food);
            cleaningService.scheduleCleaning(roomType);
        }

        public void organizeEvent(String eventName, int participants, String roomType, String equipment) {
            System.out.println("\nСценарий: организация мероприятия");
            eventManagementSystem.bookHall(eventName);
            eventManagementSystem.orderEquipment(equipment);
            for (int i = 1; i <= participants; i++) {
                roomBookingSystem.bookRoom("Участник " + i, roomType);
            }
        }

        public void reserveRestaurantWithTaxi(String guestName, int persons) {
            System.out.println("\nСценарий: бронирование стола с такси");
            restaurantSystem.bookTable(guestName, persons);
            taxiService.callTaxi(guestName);
        }

        public void cancelRoomBooking(String guestName) {
            System.out.println("\nСценарий: отмена бронирования");
            roomBookingSystem.cancelBooking(guestName);
        }

        public void requestCleaning(String roomType) {
            System.out.println("\nСценарий: уборка по запросу");
            cleaningService.cleanNow(roomType);
        }
    }

    static abstract class OrganizationComponent {
        protected String name;

        public OrganizationComponent(String name) {
            this.name = name;
        }

        public void add(OrganizationComponent component) {
            throw new UnsupportedOperationException();
        }

        public void remove(OrganizationComponent component) {
            throw new UnsupportedOperationException();
        }

        public abstract void display(String indent);
        public abstract double getBudget();
        public abstract int getEmployeeCount();
        public abstract Employee findEmployee(String employeeName);
        public abstract void listEmployees(List<Employee> employees);
    }

    static class Employee extends OrganizationComponent {
        protected String position;
        protected double salary;

        public Employee(String name, String position, double salary) {
            super(name);
            this.position = position;
            this.salary = salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
            System.out.println("Новая зарплата сотрудника " + name + ": " + salary);
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "Сотрудник: " + name + ", должность: " + position + ", зарплата: " + salary);
        }

        @Override
        public double getBudget() {
            return salary;
        }

        @Override
        public int getEmployeeCount() {
            return 1;
        }

        @Override
        public Employee findEmployee(String employeeName) {
            if (name.equalsIgnoreCase(employeeName)) {
                return this;
            }
            return null;
        }

        @Override
        public void listEmployees(List<Employee> employees) {
            employees.add(this);
        }
    }

    static class Contractor extends Employee {
        public Contractor(String name, String position, double salary) {
            super(name, position, salary);
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "Контрактор: " + name + ", роль: " + position + ", фиксированная оплата: " + salary);
        }

        @Override
        public double getBudget() {
            return 0;
        }
    }

    static class Department extends OrganizationComponent {
        private List<OrganizationComponent> components = new ArrayList<>();

        public Department(String name) {
            super(name);
        }

        @Override
        public void add(OrganizationComponent component) {
            if (component == null) {
                System.out.println("Нельзя добавить null в отдел " + name);
                return;
            }
            if (components.contains(component)) {
                System.out.println("Компонент " + component.name + " уже есть в отделе " + name);
                return;
            }
            components.add(component);
        }

        @Override
        public void remove(OrganizationComponent component) {
            if (component == null) {
                System.out.println("Нельзя удалить null из отдела " + name);
                return;
            }
            if (!components.contains(component)) {
                System.out.println("Компонент " + component.name + " не найден в отделе " + name);
                return;
            }
            components.remove(component);
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "Отдел: " + name + ", бюджет: " + getBudget() + ", сотрудников: " + getEmployeeCount());
            for (OrganizationComponent component : components) {
                component.display(indent + "  ");
            }
        }

        @Override
        public double getBudget() {
            double total = 0;
            for (OrganizationComponent component : components) {
                total += component.getBudget();
            }
            return total;
        }

        @Override
        public int getEmployeeCount() {
            int total = 0;
            for (OrganizationComponent component : components) {
                total += component.getEmployeeCount();
            }
            return total;
        }

        @Override
        public Employee findEmployee(String employeeName) {
            for (OrganizationComponent component : components) {
                Employee found = component.findEmployee(employeeName);
                if (found != null) {
                    return found;
                }
            }
            return null;
        }

        @Override
        public void listEmployees(List<Employee> employees) {
            for (OrganizationComponent component : components) {
                component.listEmployees(employees);
            }
        }
    }

    public static void main(String[] args) {
        RoomBookingSystem roomBookingSystem = new RoomBookingSystem();
        RestaurantSystem restaurantSystem = new RestaurantSystem();
        EventManagementSystem eventManagementSystem = new EventManagementSystem();
        CleaningService cleaningService = new CleaningService();
        TaxiService taxiService = new TaxiService();

        HotelFacade hotelFacade = new HotelFacade(
                roomBookingSystem,
                restaurantSystem,
                eventManagementSystem,
                cleaningService,
                taxiService
        );

        hotelFacade.bookRoomWithServices("Алия", "Люкс", "Паста");
        hotelFacade.organizeEvent("IT Conference", 3, "Стандарт", "Проектор и микрофоны");
        hotelFacade.reserveRestaurantWithTaxi("Руслан", 2);
        hotelFacade.cancelRoomBooking("Алия");
        hotelFacade.requestCleaning("Люкс");

        System.out.println("\n================ Корпоративная структура ================\n");

        Department company = new Department("Компания");
        Department itDepartment = new Department("IT отдел");
        Department hrDepartment = new Department("HR отдел");
        Department devTeam = new Department("Команда разработки");

        Employee emp1 = new Employee("Айдана", "HR менеджер", 350000);
        Employee emp2 = new Employee("Нурсултан", "Системный администратор", 500000);
        Employee emp3 = new Employee("Диас", "Java разработчик", 600000);
        Employee emp4 = new Employee("Аружан", "Frontend разработчик", 550000);
        Contractor contractor = new Contractor("Тимур", "UI/UX консультант", 300000);

        hrDepartment.add(emp1);
        devTeam.add(emp3);
        devTeam.add(emp4);
        devTeam.add(contractor);
        itDepartment.add(emp2);
        itDepartment.add(devTeam);

        company.add(hrDepartment);
        company.add(itDepartment);

        company.display("");

        System.out.println("\nИзменение зарплаты:");
        emp3.setSalary(650000);
        company.display("");

        System.out.println("\nПоиск сотрудника:");
        Employee found = company.findEmployee("Диас");
        if (found != null) {
            found.display("");
        } else {
            System.out.println("Сотрудник не найден");
        }

        System.out.println("\nСписок всех сотрудников компании:");
        List<Employee> allEmployees = new ArrayList<>();
        company.listEmployees(allEmployees);
        for (Employee employee : allEmployees) {
            System.out.println(employee.name + " - " + employee.position);
        }
    }
}