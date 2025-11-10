package com.bigcompany.model;

public class SalaryReport {
  private final EmployeeRecord employee;
  private final String status; // "OVERPAID" or "UNDERPAID"
  private final double currentSalary;
  private final double expectedSalary;

  public SalaryReport(
      EmployeeRecord employee, String status, double currentSalary, double expectedSalary) {
    this.employee = employee;
    this.status = status;
    this.currentSalary = currentSalary;
    this.expectedSalary = expectedSalary;
  }

  public EmployeeRecord getEmployee() {
    return employee;
  }

  public String getStatus() {
    return status;
  }

  public double getCurrentSalary() {
    return currentSalary;
  }

  public double getExpectedSalary() {
    return expectedSalary;
  }

  @Override
  public String toString() {
    if (status.equals("UNDERPAID")) {
      return String.format(
          "UNDERPAID: %s earns %.2f, and is underpaid by %.2f (min expected %.2f)",
          employee, currentSalary, expectedSalary - currentSalary, expectedSalary);
    } else {
      return String.format(
          "OVERPAID: %s earns %.2f, and is overpaid by %.2f (max expected %.2f)",
          employee, currentSalary, currentSalary - expectedSalary, expectedSalary);
    }
  }
}
