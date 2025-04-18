package com.example.demo.entity;

public class EmployeeProject {
    private long empId; // Foreign key from Employee
    private int projId; // Foreign key from Project

    // Constructor
    public EmployeeProject(long empId, int projId) {
        this.empId = empId;
        this.projId = projId;
    }

    // Getters and Setters
    public long getEmpId() {
        return empId;
    }

    public void setEmpId(long empId) {
        this.empId = empId;
    }

    public int getProjId() {
        return projId;
    }

    public void setProjId(int projId) {
        this.projId = projId;
    }
}