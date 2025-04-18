package com.example.demo.entity;

import java.time.LocalDate;

public class LeaveRequest {
    private Long empId;            // Employee ID
    private String leaveType;      // Type of leave (e.g., Casual, Sick)
    private LocalDate startDate;   // Start date of the leave
    private LocalDate endDate;     // End date of the leave
    private String status;         // Status of the leave request (e.g., "Pending", "Approved", "Rejected")
    private String reason;         // Reason for the leave

    // Getters and Setters
    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
