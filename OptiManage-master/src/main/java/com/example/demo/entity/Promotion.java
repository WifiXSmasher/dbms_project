package com.example.demo.entity;

public class Promotion {
    private Long id;
    private Long empId;
    private String previousPosition;
    private String newPosition;
    private double appraisalAmount;
    // private Date promotionDate;
    // Getters and setters
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

    public String getPreviousPosition() {
        return previousPosition;
    }

    public void setPreviousPosition(String previousPosition) {
        this.previousPosition = previousPosition;
    }

    public String getNewPosition() {
        return newPosition;
    }

    public void setNewPosition(String newPosition) {
        this.newPosition = newPosition;
    }

    public double getAppraisalAmount() {
        return appraisalAmount;
    }

    public void setAppraisalAmount(double appraisalAmount) {
        this.appraisalAmount = appraisalAmount;
    }
}
