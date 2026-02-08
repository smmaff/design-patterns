package OCP;
public class OcpDemo {
    public static void main(String[] args) {
        Employee e = new Employee("Alex", 1000);

        EmployeeSalaryCalculator calcPermanent = new EmployeeSalaryCalculator(new PermanentSalaryPolicy());
        System.out.println("Permanent salary: " + calcPermanent.calculate(e));

        EmployeeSalaryCalculator calcFreelancer = new EmployeeSalaryCalculator(new FreelancerSalaryPolicy());
        System.out.println("Freelancer salary: " + calcFreelancer.calculate(e));
    }
}