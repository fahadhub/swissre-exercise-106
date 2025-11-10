package com.bigcompany.service;

import com.bigcompany.model.DepthReport;
import com.bigcompany.model.EmployeeRecord;
import com.bigcompany.model.SalaryReport;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class OrganizationAnalyzerTest {

    private final OrganizationAnalyzer analyzer = new OrganizationAnalyzer();

    // Helper to quickly create a manager with subordinates
    private EmployeeRecord createManagerWithSubs(double managerSalary, double... subSalaries) {
        EmployeeRecord manager = new EmployeeRecord(1, "Alice", "Manager", managerSalary, null);
        int id = 2;
        for (double s : subSalaries) {
            EmployeeRecord emp = new EmployeeRecord(id++, "Emp" + id, "Staff", s, 1);
            manager.getSubordinates().add(emp);
        }
        return manager;
    }

    // Manager correctly paid
    @Test
    void testManagerCorrectlyPaid() {
        EmployeeRecord manager = createManagerWithSubs(120, 80, 100); // avg=90, range=108–135
        List<SalaryReport> reports = analyzer.checkManagerSalaries(manager);
        assertTrue(reports.isEmpty(), "Manager in valid range should not trigger report");
    }

    // Manager underpaid
    @Test
    void testUnderpaidManager() {
        EmployeeRecord manager = createManagerWithSubs(100, 100, 100); // avg=100, should be ≥120
        List<SalaryReport> reports = analyzer.checkManagerSalaries(manager);
        assertEquals(1, reports.size());
        assertEquals("UNDERPAID", reports.get(0).getStatus());
        assertEquals(manager, reports.get(0).getEmployee());
    }

    // Manager overpaid
    @Test
    void testOverpaidManage() {
        EmployeeRecord manager = createManagerWithSubs(200, 80, 100); // avg=90, should be ≤135
        List<SalaryReport> reports = analyzer.checkManagerSalaries(manager);
        assertEquals(1, reports.size());
        assertEquals("OVERPAID", reports.get(0).getStatus());
        assertEquals(manager, reports.get(0).getEmployee());
    }

    // Multi-level hierarchy
    @Test
    void testMultiLevelHierarchy_MultipleReports() {
        EmployeeRecord ceo = new EmployeeRecord(1, "CEO", "Boss", 1000, null);
        EmployeeRecord mgr = new EmployeeRecord(2, "Mgr", "Middle", 200, 1);
        EmployeeRecord e1 = new EmployeeRecord(3, "Emp1", "Staff", 100, 2);
        EmployeeRecord e2 = new EmployeeRecord(4, "Emp2", "Staff", 100, 2);
        ceo.getSubordinates().add(mgr);
        mgr.getSubordinates().addAll(List.of(e1, e2));

        List<SalaryReport> reports = analyzer.checkManagerSalaries(ceo);
        assertTrue(reports.size() >= 1, "Should detect underpaid middle manager or overpaid CEO");
    }

    // Leaf employee
    @Test
    void testLeafEmployee() {
        EmployeeRecord emp = new EmployeeRecord(1, "Solo", "Worker", 100, null);
        List<SalaryReport> reports = analyzer.checkManagerSalaries(emp);
        assertTrue(reports.isEmpty());
    }

    // Null root
    @Test
    void testNullRootSalaryAnalysis() {
        List<SalaryReport> reports = analyzer.checkManagerSalaries(null);
        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }

    // Shallow hierarchy (≤4 levels)
    @Test
    void testShallowHierarchy_NoDepthReport() {
        EmployeeRecord ceo = new EmployeeRecord(1, "A", "CEO", 100, null);
        EmployeeRecord e2 = new EmployeeRecord(2, "B", "Mgr", 90, 1);
        EmployeeRecord e3 = new EmployeeRecord(3, "C", "Lead", 80, 2);
        EmployeeRecord e4 = new EmployeeRecord(4, "D", "Dev", 70, 3);
        ceo.getSubordinates().add(e2);
        e2.getSubordinates().add(e3);
        e3.getSubordinates().add(e4);

        List<DepthReport> reports = analyzer.checkReportingDepth(ceo);
        assertTrue(reports.isEmpty());
    }

    // Deep hierarchy (>4 levels)
    @Test
    void testDeepHierarchy() {
        EmployeeRecord ceo = new EmployeeRecord(1, "A", "CEO", 100, null);
        EmployeeRecord e2 = new EmployeeRecord(2, "B", "M1", 90, 1);
        EmployeeRecord e3 = new EmployeeRecord(3, "C", "M2", 80, 2);
        EmployeeRecord e4 = new EmployeeRecord(4, "D", "M3", 70, 3);
        EmployeeRecord e5 = new EmployeeRecord(5, "E", "M4", 60, 4);
        EmployeeRecord e6 = new EmployeeRecord(6, "F", "Dev", 50, 5);

        ceo.getSubordinates().add(e2);
        e2.getSubordinates().add(e3);
        e3.getSubordinates().add(e4);
        e4.getSubordinates().add(e5);
        e5.getSubordinates().add(e6);

        List<DepthReport> reports = analyzer.checkReportingDepth(ceo);

        assertEquals(1, reports.size());
        assertEquals(e6, reports.get(0).getEmployee());
        assertEquals(5, reports.get(0).getLevel());
    }

    // Multiple deep employees
    @Test
    void testMultipleDeepEmployees() {
        EmployeeRecord ceo = new EmployeeRecord(1, "A", "CEO", 100, null);

        EmployeeRecord m1 = new EmployeeRecord(2, "B", "M1", 90, 1);
        EmployeeRecord m2 = new EmployeeRecord(3, "C", "M2", 80, 2);
        EmployeeRecord m3 = new EmployeeRecord(4, "D", "M3", 70, 3);
        EmployeeRecord m4 = new EmployeeRecord(5, "E", "M4", 60, 4);
        EmployeeRecord emp1 = new EmployeeRecord(6, "F", "Dev", 50, 5);

        EmployeeRecord m11 = new EmployeeRecord(7, "G", "M1b", 90, 1);
        EmployeeRecord m12 = new EmployeeRecord(8, "H", "M2b", 80, 7);
        EmployeeRecord m13 = new EmployeeRecord(9, "I", "M3b", 70, 8);
        EmployeeRecord m14 = new EmployeeRecord(10, "J", "M4b", 60, 9);
        EmployeeRecord emp2 = new EmployeeRecord(11, "K", "Dev2", 50, 10);

        ceo.getSubordinates().addAll(List.of(m1, m11));
        m1.getSubordinates().add(m2);
        m2.getSubordinates().add(m3);
        m3.getSubordinates().add(m4);
        m4.getSubordinates().add(emp1);

        m11.getSubordinates().add(m12);
        m12.getSubordinates().add(m13);
        m13.getSubordinates().add(m14);
        m14.getSubordinates().add(emp2);

        List<DepthReport> reports = analyzer.checkReportingDepth(ceo);
        assertEquals(2, reports.size(), "Both deep employees should be detected");
    }

    @Test
    void testNullRootDepthAnalysis() {
        List<DepthReport> reports = analyzer.checkReportingDepth(null);
        assertNotNull(reports);
        assertTrue(reports.isEmpty());
    }
}
