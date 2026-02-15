package homework4.Vehicle;
import java.util.Scanner;

interface IVehicle{
    void drive();
    void refuel();
}
class Car implements IVehicle{
    private String brand;
    private String model;
    private String fuelType;
    public Car(String brand,String model,String fuelType){
        this.brand=brand;
        this.model=model;
        this.fuelType=fuelType;
    }
    @Override
    public void drive(){
        System.out.println("Car "+brand+" "+model+" is driving on "+fuelType);
    }
    @Override
    public void refuel(){
        System.out.println("Car "+brand+" "+model+" is refueling with "+fuelType);
    }
}
class Motorcycle implements IVehicle{
    private String type;
    private int engineCapacity;
    public Motorcycle(String type,int engineCapacity){
        this.type=type;
        this.engineCapacity=engineCapacity;
    }
    @Override
    public void drive(){
        System.out.println("Motorcycle "+type+" "+engineCapacity+"cc is driving");
    }
    @Override
    public void refuel(){
        System.out.println("Motorcycle "+type+" "+engineCapacity+"cc is refueling");
    }
}
class Truck implements IVehicle{
    private double loadCapacity;
    private int axles;
    public Truck(double loadCapacity,int axles){
        this.loadCapacity=loadCapacity;
        this.axles=axles;
    }
    @Override
    public void drive(){
        System.out.println("Truck with capacity "+loadCapacity+" tons and "+axles+" axles is driving");
    }
    @Override
    public void refuel(){
        System.out.println("Truck with capacity "+loadCapacity+" tons and "+axles+" axles is refueling");
    }
}

class Bus implements IVehicle{
    private int seats;
    private String route;
    public Bus(int seats,String route){
        this.seats=seats;
        this.route=route;
    }
    @Override
    public void drive(){
        System.out.println("Bus with "+seats+" seats is driving on route "+route);
    }
    @Override
    public void refuel(){
        System.out.println("Bus with "+seats+" seats on route "+route+" is refueling");
    }
}
abstract class VehicleFactory{
    public abstract IVehicle createVehicle();
}
class CarFactory extends VehicleFactory{
    private String brand;
    private String model;
    private String fuelType;
    public CarFactory(String brand,String model,String fuelType){
        this.brand=brand;
        this.model=model;
        this.fuelType=fuelType;
    }
    @Override
    public IVehicle createVehicle(){
        return new Car(brand,model,fuelType);
    }
}
class MotorcycleFactory extends VehicleFactory{
    private String type;
    private int engineCapacity;
    public MotorcycleFactory(String type,int engineCapacity){
        this.type=type;
        this.engineCapacity=engineCapacity;
    }
    @Override
    public IVehicle createVehicle(){
        return new Motorcycle(type,engineCapacity);
    }
}
class TruckFactory extends VehicleFactory{
    private double loadCapacity;
    private int axles;
    public TruckFactory(double loadCapacity,int axles){
        this.loadCapacity=loadCapacity;
        this.axles=axles;
    }
    @Override
    public IVehicle createVehicle(){
        return new Truck(loadCapacity,axles);
    }
}
class BusFactory extends VehicleFactory{
    private int seats;
    private String route;
    public BusFactory(int seats,String route){
        this.seats=seats;
        this.route=route;
    }
    @Override
    public IVehicle createVehicle(){
        return new Bus(seats,route);
    }
}
public class VehicleApp{
    public static void main(String[] args){
        Scanner scanner=new Scanner(System.in);
        System.out.println("Select vehicle type: car, motorcycle, truck, bus");
        String type=scanner.nextLine().trim().toLowerCase();
        VehicleFactory factory;
        switch(type){
            case "car":
                System.out.println("Enter brand:");
                String brand=scanner.nextLine();
                System.out.println("Enter model:");
                String model=scanner.nextLine();
                System.out.println("Enter fuel type:");
                String fuel=scanner.nextLine();
                factory=new CarFactory(brand,model,fuel);
                break;
            case "motorcycle":
                System.out.println("Enter motorcycle type (sport,touring,...):");
                String motoType=scanner.nextLine();
                System.out.println("Enter engine capacity (cc):");
                int cc=Integer.parseInt(scanner.nextLine());
                factory=new MotorcycleFactory(motoType,cc);
                break;
            case "truck":
                System.out.println("Enter load capacity (tons):");
                double capacity=Double.parseDouble(scanner.nextLine());
                System.out.println("Enter number of axles:");
                int axles=Integer.parseInt(scanner.nextLine());
                factory=new TruckFactory(capacity,axles);
                break;
            case "bus":
                System.out.println("Enter seats count:");
                int seats=Integer.parseInt(scanner.nextLine());
                System.out.println("Enter route:");
                String route=scanner.nextLine();
                factory=new BusFactory(seats,route);
                break;
            default:
                System.out.println("Unknown vehicle type");
                scanner.close();
                return;
        }
        IVehicle vehicle=factory.createVehicle();
        vehicle.drive();
        vehicle.refuel();
        scanner.close();
    }
}
