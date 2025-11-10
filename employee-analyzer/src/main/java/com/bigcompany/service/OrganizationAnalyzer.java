package com.bigcompany.service;

import com.bigcompany.model.DepthReport;
import com.bigcompany.model.EmployeeRecord;
import com.bigcompany.model.SalaryReport;

import java.util.*;


// Core business logic for analyzing company hierarchy and salary structure.
public class OrganizationAnalyzer {

    // Checks each manager's salary to ensure it’s within 20–50% above their subordinates' average.
    public List<SalaryReport> checkManagerSalaries(EmployeeRecord root) {
        List<SalaryReport> reports = new ArrayList<>();
        if (root != null) analyzeManagerSalary(root, reports);;
        return reports;
    }

    private void analyzeManagerSalary(EmployeeRecord manager, List<SalaryReport> reports) {
        if (manager.getSubordinates().isEmpty()) return;

        double avgSubordinateSalary = 0.0;
        List<EmployeeRecord> subs = manager.getSubordinates();

        if (!subs.isEmpty()) {
            double sum = 0.0;
            for (EmployeeRecord e : subs) {
                sum += e.getSalary();
            }
            avgSubordinateSalary = sum / subs.size();
        }

        double minExpected = avgSubordinateSalary * 1.2;
        double maxExpected = avgSubordinateSalary * 1.5;
        double salary = manager.getSalary();

        if (salary < minExpected) {
            reports.add(new SalaryReport(manager, "UNDERPAID", salary, minExpected));
        } else if (salary > maxExpected) {
            reports.add(new SalaryReport(manager, "OVERPAID", salary, maxExpected));
        }

        for (EmployeeRecord e : manager.getSubordinates()) {
            analyzeManagerSalary(e, reports);
        }
    }

    // Checks employees who have more than 4 managers above them (deep hierarchy).
    public List<DepthReport> checkReportingDepth(EmployeeRecord root) {
        List<DepthReport> deepReports = new ArrayList<>();
        if (root != null) detectDeepReports(root, 0, deepReports);
        return deepReports;
    }

    private void detectDeepReports(EmployeeRecord emp, int level, List<DepthReport> deepReports) {
        if (level > 4) {
            deepReports.add(new DepthReport(emp, level));
        }

        for (EmployeeRecord sub : emp.getSubordinates()) {
            detectDeepReports(sub, level + 1, deepReports);
        }
    }
}
