package com.bigcompany.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bigcompany.model.EmployeeRecord;
import com.bigcompany.parser.CsvParser;
import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

public class BuildHierarchyTest {
  @Test
  void testNoCeoThrowsException() {
    BuildHierarchy builder = new BuildHierarchy();

    Map<Integer, EmployeeRecord> employees = new HashMap<>();
    employees.put(124, new EmployeeRecord(124, "Martin", "Chekov", 45000, 123));
    employees.put(125, new EmployeeRecord(125, "Bob", "Ronstad", 47000, 123));

    IllegalStateException ex =
        assertThrows(
            IllegalStateException.class,
            () -> {
              builder.buildHierarchy(employees);
            });

    assertEquals("No CEO found (employee with null managerId)", ex.getMessage());
  }

  @Test
  void testMissingManagerIdHandledGracefully() throws Exception {
    String csvData =
        """
                Id,firstName,lastName,salary,managerId
                123,Joe,Doe,60000,
                124,Martin,Chekov,45000,9999
                """;

    File tempFile = File.createTempFile("missingManager", ".csv");
    try (FileWriter fw = new FileWriter(tempFile)) {
      fw.write(csvData);
    }

    CsvParser parser = new CsvParser();
    Map<Integer, EmployeeRecord> employees = parser.parse(tempFile.getAbsolutePath());
    BuildHierarchy builder = new BuildHierarchy();
    EmployeeRecord ceo = builder.buildHierarchy(employees);

    assertNotNull(ceo);
    assertEquals(123, ceo.getId());
    assertEquals(0, ceo.getSubordinates().size());

    tempFile.delete();
  }
}
