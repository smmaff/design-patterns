package OCP;
class InternSalaryPolicy implements SalaryPolicy {
    @Override
    public double calculateSalary(Employee employee) {
        return employee.getBaseSalary() * 0.8; // 80%
    }
    @Override
    public String typeName() { return "Intern"; }
}
