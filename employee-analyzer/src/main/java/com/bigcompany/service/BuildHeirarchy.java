package com.bigcompany.service;

import java.util.Map;

import com.bigcompany.model.EmployeeRecord;

public class BuildHeirarchy {
    public EmployeeRecord buildHierarchy(Map<Integer, EmployeeRecord> employees) {
        EmployeeRecord ceo = null;

        for (EmployeeRecord e : employees.values()) {
            if (e.getManagerId() == null) {
                ceo = e;
            } else {
                EmployeeRecord manager = employees.get(e.getManagerId());
                if (manager != null) {
                    manager.getSubordinates().add(e);
                }
            }
        }
        if (ceo == null) {
            throw new IllegalStateException("No CEO found (employee with null managerId)");
        }
        return ceo;
    }
    
}
