package OCP;
class EmployeeSalaryCalculator {
    private final SalaryPolicy salaryPolicy;

    public EmployeeSalaryCalculator(SalaryPolicy salaryPolicy) {
        this.salaryPolicy = salaryPolicy;
    }

    public double calculate(Employee employee) {
        return salaryPolicy.calculateSalary(employee);
    }
}