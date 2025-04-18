package com.example.demo.dto;
public class PromotionDTO {
    private Long id;
    private String currentPosition;
    private String newPosition;
    private double appraisalAmount;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
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

