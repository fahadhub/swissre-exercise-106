package com.bigcompany.model;

public class DepthReport {
  private final EmployeeRecord employee;
  private final int level;

  public DepthReport(EmployeeRecord employee, int level) {
    this.employee = employee;
    this.level = level;
  }

  public EmployeeRecord getEmployee() {
    return employee;
  }

  public int getLevel() {
    return level;
  }

  @Override
  public String toString() {
    return String.format("TOO DEEP: %s has %d managers above", employee, level);
  }
}
