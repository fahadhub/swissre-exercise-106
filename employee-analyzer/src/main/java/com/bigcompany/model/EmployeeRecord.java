package com.bigcompany.model;

import java.util.ArrayList;
import java.util.List;

// Represents an employee in the organization.
// Each employee may have a manager and a list of direct subordinates.
public class EmployeeRecord {
  private final int id;
  private final String firstName;
  private final String lastName;
  private final double salary;
  private final Integer managerId; // null if CEO
  private final List<EmployeeRecord> subordinates = new ArrayList<>();

  public EmployeeRecord(
      int id, String firstName, String lastName, double salary, Integer managerId) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
    this.managerId = managerId;
  }

  public int getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public double getSalary() {
    return salary;
  }

  public Integer getManagerId() {
    return managerId;
  }

  public List<EmployeeRecord> getSubordinates() {
    return subordinates;
  }

  @Override
  public String toString() {
    return firstName + " " + lastName + " (ID: " + id + ")";
  }
}
