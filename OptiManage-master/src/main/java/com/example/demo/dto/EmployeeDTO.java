package com.example.demo.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

// import java.util.Date;

public class EmployeeDTO {

    private Long id; // ID

    @Size(min = 3, message = "Name must be at least 3 characters long")
    @NotNull(message = "Name can not be empty")
    private String firstName; // First Name

    private String lastName; // Last Name
    private String address; // Address

    @Pattern(regexp = "^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$", message = "Invalid email format")
    @NotNull(message = "Email cannot be empty")
    private String email;// Email

    private Double salary; // Salary
    private Integer deptId; // Department ID (Foreign Key)
   
    @Pattern(regexp = "^\\d{10}$", message = "Contact number must be 10 digits")
    private String contactNumber; // Contact Number
    private LocalDate dob; // Date of Birth
    
    @Size(min = 3, message = "Name must be at least 3 characters long")
    @NotNull(message = "Name can not be empty")
    private String username; // Username
   
    @NotNull(message = "Password can not be empty")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[a-z])(?=.*[@#$%^&+=]).*$", message = "Password must contain at least one digit, one uppercase letter, one lowercase letter, and one special character")
    private String password; // Password (Hashed)
   
    private String gender; // Gender ('Male', 'Female', 'Other')
    private String role; // Role (e.g., Manager, Developer)
    private String emergencyContact; // Emergency Contact Number
    private Integer age; // Age (Can be computed from DOB in application logic)
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

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

    public void setDob(LocalDate date) {
        this.dob = date;
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
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
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
