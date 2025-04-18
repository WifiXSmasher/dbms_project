package com.example.demo.entity;

import java.sql.Date;

public class Project {
    private int projId;
    private String pname;
    private String description;
    private Date startDate;
    private Date endDate;
    private String status;
    private int deptId;
    private Date deadline;
    private Long leadBy; // Change from int to Long to match the emp_id type

    // Getters and Setters
    public int getProjId() {
        return projId;
    }

    public void setProjId(int projId) {
        this.projId = projId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }
    public Long getLeadBy() { return leadBy; } // Update getter to return Long
    public void setLeadBy(Long leadBy) { this.leadBy = leadBy; } // Update setter to accept Long
}