package com.example.demo.entity;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.format.annotation.DateTimeFormat;

public class Employee {

    private Long id;  // ID
    private String firstName;  // First Name
    private String lastName;  // Last Name
    private String address;  // Address
    private String email;  // Email
    // private Double salary;  // Salary
    private Integer deptId;  // Department ID (Foreign Key)
    private String contactNumber;  // Contact Number
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Ensure the format matches
    private LocalDate dob;  // Date of Birth
    private String username;  // Username
    private String password;  // Password (Hashed)
    private String gender;  // Gender ('Male', 'Female', 'Other')
    private String role;  // Role (e.g., Manager, Developer)
    private String emergencyContact;  // Emergency Contact Number
    // private Integer age;  // Age (Can be computed from DOB in application logic)
    private Long reportsTo;  // New 'reports_to' field
    private String departmentName;
    private String position;
    private String bankAccountNumber;

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // public Double getSalary() {
    //     return salary;
    // }

    // public void setSalary(Double salary) {
    //     this.salary = salary;
    // }

    public Integer getDeptId() {
        return deptId;
    }

    public void setDeptId(Integer deptId) {
        this.deptId = deptId;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public Integer getAge() {
        if (this.dob == null) {
            return null; // or return a default age, if applicable
        }
        return Period.between(this.dob, LocalDate.now()).getYears();
    }


    public Long getreportsTo() {
        return reportsTo;
    }

    public void setreportsTo(Long reportsTo) {
        this.reportsTo = reportsTo;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getPosition(){
        return position;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public String getBankAccountNumber(){
        return bankAccountNumber;
    }

    public void setbankAccountNumber(String bankAccountNumber){
        this.bankAccountNumber = bankAccountNumber;
    }
}
