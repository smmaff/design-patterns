package OCP;
class ContractSalaryPolicy implements SalaryPolicy {
    @Override
    public double calculateSalary(Employee employee) {
        return employee.getBaseSalary() * 1.1; // +10%
    }
    @Override
    public String typeName() { return "Contract"; }
}