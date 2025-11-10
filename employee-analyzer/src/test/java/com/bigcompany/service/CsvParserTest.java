package com.bigcompany.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.bigcompany.parser.CsvParser;
import com.bigcompany.model.EmployeeRecord;

public class CsvParserTest {
    @Test
    void testLoadEmployeesParsesCsvCorrectly() throws IOException {
        String csvContent = """
                Id,firstName,lastName,salary,managerId
                123,Joe,Doe,60000,
                124,Martin,Chekov,45000,123
                125,Bob,Ronstad,47000,123
                300,Alice,Hasacat,50000,124
                305,Brett,Hardleaf,34000,300
                """;

        Path tempFile = Files.createTempFile("employees", ".csv");
        Files.writeString(tempFile, csvContent);

        CsvParser parser = new CsvParser();
        Map<Integer, EmployeeRecord> employees = parser.parse(tempFile.toString());

        assertEquals(5, employees.size(), "Should load 5 employees");
        assertNull(employees.get(123).getManagerId(), "CEO should have null managerId");
        assertEquals(124, employees.get(300).getManagerId(), "Alice should report to Martin");

        Files.deleteIfExists(tempFile);
    }

    @Test
    void testMalformedCsvLineThrowsNumberFormatException() throws Exception {
        String csvData = """
                Id,firstName,lastName,salary,managerId
                124,Martin,Chekov,45000,123
                125,Bob,Ronstad,abc,123
                """;

        File tempFile = File.createTempFile("malformed", ".csv");
        try (FileWriter fw = new FileWriter(tempFile)) {
            fw.write(csvData);
        }

        CsvParser parser = new CsvParser();

        assertThrows(NumberFormatException.class, () -> {
            parser.parse(tempFile.getAbsolutePath());
        });

        tempFile.delete();
    }
    
}
