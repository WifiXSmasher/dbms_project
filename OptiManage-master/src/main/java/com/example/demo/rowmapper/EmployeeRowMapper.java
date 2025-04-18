package com.example.demo.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.example.demo.entity.Employee;


public class EmployeeRowMapper implements RowMapper<Employee> {

    @Override
    public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
        Employee employee = new Employee();
        employee.setId(rs.getLong("id"));  // ID
        employee.setFirstName(rs.getString("first_name"));  // First Name
        employee.setLastName(rs.getString("last_name"));  // Last Name
        employee.setAddress(rs.getString("address"));  // Address
        employee.setEmail(rs.getString("email"));  // Email
        employee.setContactNumber(rs.getString("contact_number"));  // Contact Number
        // employee.setSalary(rs.getDouble("salary"));  // Salary
        employee.setDeptId(rs.getInt("dept_id"));  // Department ID (Foreign Key)
        employee.setDob(rs.getDate("dob").toLocalDate());  // Date of Birth
        employee.setUsername(rs.getString("username"));  // Username
        employee.setPassword(rs.getString("password"));  // Password (Hashed)
        employee.setGender(rs.getString("gender"));  // Gender ('Male', 'Female', 'Other')
        employee.setRole(rs.getString("role"));  // Role
        employee.setEmergencyContact(rs.getString("emergency_contact"));  // Emergency Contact Number
        // employee.setAge(rs.getInt("age"));  // Age
        employee.setreportsTo(rs.getLong("reports_to"));  // Emergency Contact
        employee.setPosition(rs.getString("position"));
        employee.setbankAccountNumber(rs.getString("bankAccountNumber"));
        return employee;
    }
}
