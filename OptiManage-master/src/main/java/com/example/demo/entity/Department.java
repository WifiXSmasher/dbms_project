package com.example.demo.entity;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class Department {
    private int deptId;
    private String dname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date estDate;
    private String contactDetail;
    private String email;
    private Long hodId;  // Reference to HOD

    // Getters and Setters
    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public String getDname() {
        return dname;
    }

    public void setDname(String dname) {
        this.dname = dname;
    }

    public Date getEstDate() {
        return estDate;
    }

    public void setEstDate(Date estDate) {
        this.estDate = estDate;
    }

    public String getContactDetail() {
        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {
        this.contactDetail = contactDetail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getHodId() {
        return hodId;
    }

    public void setHodId(Long hodId) {
        this.hodId = hodId;
    }
}
