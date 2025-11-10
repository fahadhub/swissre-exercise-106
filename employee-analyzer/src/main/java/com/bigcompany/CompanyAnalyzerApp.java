package com.bigcompany;

import com.bigcompany.model.DepthReport;
import com.bigcompany.model.EmployeeRecord;
import com.bigcompany.model.SalaryReport;

import com.bigcompany.parser.CsvParser;
import com.bigcompany.service.OrganizationAnalyzer;
import com.bigcompany.service.BuildHeirarchy;

import java.io.IOException;
import java.util.*;

// Entry point for the Company Analyzer application.
// Usage: java -jar employee-analyzer.jar <path-to-csv-file>

public class CompanyAnalyzerApp {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java -jar employee-analyzer.jar <path-to-csv-file>");
            System.exit(1);
        }

        String filePath = args[0];
        OrganizationAnalyzer analyzer = new OrganizationAnalyzer();
        CsvParser parser = new CsvParser();
        BuildHeirarchy builder = new BuildHeirarchy();

        try {
            Map<Integer, EmployeeRecord> employees = parser.parse(filePath);
            EmployeeRecord ceo = builder.buildHierarchy(employees);

            List<SalaryReport> salaryReports = analyzer.checkManagerSalaries(ceo);
            for (SalaryReport report : salaryReports) {
                System.out.println(report.toString());
            }
            List<DepthReport> depthReports = analyzer.checkReportingDepth(ceo);
            for (DepthReport dr : depthReports) {
                System.out.println(dr.toString());
            }

        } catch (IllegalStateException e) {
            System.err.println("Hierarchy error: " + e.getMessage());
            System.exit(2);
        } catch (IOException e) {
            System.err.println("Error reading employee data: " + e.getMessage());
            System.exit(3);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            System.exit(99);
        }
    }
}
