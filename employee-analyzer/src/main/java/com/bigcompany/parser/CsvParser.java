package com.bigcompany.parser;

import com.bigcompany.model.EmployeeRecord;
import java.io.*;
import java.util.*;

public class CsvParser {

  // Reads employee data from a CSV file and returns a map keyed by employee ID.
  public Map<Integer, EmployeeRecord> parse(String filePath) throws IOException {
    Map<Integer, EmployeeRecord> employees = new HashMap<>();

    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      br.readLine(); // skip header

      while ((line = br.readLine()) != null) {
        if (line.trim().isEmpty()) continue;
        String[] parts = line.split(",");
        int id = Integer.parseInt(parts[0].trim());
        String firstName = parts[1].trim();
        String lastName = parts[2].trim();
        double salary = Double.parseDouble(parts[3].trim());
        Integer managerId =
            (parts.length > 4 && !parts[4].trim().isEmpty())
                ? Integer.parseInt(parts[4].trim())
                : null;

        EmployeeRecord emp = new EmployeeRecord(id, firstName, lastName, salary, managerId);
        employees.put(id, emp);
      }
    }
    return employees;
  }
}
