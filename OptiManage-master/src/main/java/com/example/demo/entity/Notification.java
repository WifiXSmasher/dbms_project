package com.example.demo.entity;

import java.time.LocalDateTime;

public class Notification {
    
    private Long id;
    private Long empId;  // Employee ID from employees table
    private String notif;  // Notification message content
    private LocalDateTime createdAt;  // Timestamp for notification creation

    // Constructors
    public Notification() {
    }

    public Notification(Long empId, String notif) {
        this.empId = empId;
        this.notif = notif;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getNotif() {
        return notif;
    }

    public void setNotif(String notif) {
        this.notif = notif;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
