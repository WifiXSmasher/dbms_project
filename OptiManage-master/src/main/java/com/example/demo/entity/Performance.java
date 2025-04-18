package com.example.demo.entity;

import java.time.LocalDate;

public class Performance {
    private Integer evalId;
    private long reviewee;
    private long reviewer;
    private int rating;
    private String feedback;
    private LocalDate date;
    private int projectId; // Foreign key for Project

    // Getters and setters

    public Integer getEvalId() {
        return evalId;
    }

    public void setEvalId(Integer evalId) {
        this.evalId = evalId;
    }

    public long getReviewee() {
        return reviewee;
    }

    public void setReviewee(long reviewee) {
        this.reviewee = reviewee;
    }

    public long getReviewer() {
        return reviewer;
    }

    public void setReviewer(long reviewer) {
        this.reviewer = reviewer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
}
