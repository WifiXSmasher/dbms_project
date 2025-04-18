package com.example.demo.dto;

import java.time.LocalDate;
// import java.util.Date;

public class EmployeeUpdateDTO {


    private String firstName;  // First Name
    private String lastName;  // Last Name
    private String address;  // Address
    private String email;  // Email
    private String contactNumber;  // Contact Number
    private LocalDate dob;  // Date of Birth
    private String password;  // Password (Hashed)
    private String gender;  // Gender ('Male', 'Female', 'Other')
    private String emergencyContact;  // Emergency Contact Number
    private String bankAccountNumber;


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

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getBankAccountNumber(){
        return bankAccountNumber;
    }

    public void setbankAccountNumber(String bankAccountNumber){
        this.bankAccountNumber = bankAccountNumber;
    }
}