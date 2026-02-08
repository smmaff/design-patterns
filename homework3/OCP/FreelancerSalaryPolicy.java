package OCP;
class FreelancerSalaryPolicy implements SalaryPolicy {
    @Override
    public double calculateSalary(Employee employee) {
        return employee.getBaseSalary() * 1.05; // пример: +5%
    }
    @Override
    public String typeName() { return "Freelancer"; }
}