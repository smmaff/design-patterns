package OCP;
class PermanentSalaryPolicy implements SalaryPolicy {
    @Override
    public double calculateSalary(Employee employee) {
        return employee.getBaseSalary() * 1.2; // +20%
    }
    @Override
    public String typeName() { return "Permanent"; }
}