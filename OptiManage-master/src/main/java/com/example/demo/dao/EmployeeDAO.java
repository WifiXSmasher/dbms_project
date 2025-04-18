package com.example.demo.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.example.demo.entity.Employee;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
// import java.util.stream.Collectors;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import com.example.demo.rowmapper.EmployeeRowMapper;

@Repository
public class EmployeeDAO {

    private final JdbcTemplate jdbcTemplate;

    public EmployeeDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // public List<Employee> findEmployeesExcludingHR() {
    //     String sql = "SELECT * FROM employees WHERE dept_id != 1";
    //     return jdbcTemplate.query(sql, new EmployeeRowMapper());
    // }
    
    // Method to retrieve employees grouped by department except HR
    public Map<String, List<Employee>> findAllByDepartmentExceptHR() {
        String sql = "SELECT e.*, d.dname as department_name " +
                     "FROM employees e " +
                     "JOIN Department d ON e.dept_id = d.dept_id " +
                     "WHERE e.dept_id != 1 " +
                     "ORDER BY d.dname";

        List<Employee> employees = jdbcTemplate.query(sql, new EmployeeRowMapperWithDept());
        Map<String, List<Employee>> employeesByDepartment = new HashMap<>();

        // Group employees by department name
        for (Employee employee : employees) {
            String deptName = employee.getDepartmentName();
            employeesByDepartment.computeIfAbsent(deptName, k -> new ArrayList<>()).add(employee);
        }
        
        return employeesByDepartment;
    }
    
    // Fetch all employees
    public List<Employee> findAll() {
        String sql = "SELECT * FROM employees";
        return jdbcTemplate.query(sql, new EmployeeRowMapper());
    }

    // Find employee by ID
    public Employee findById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        // return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
         try {
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
    } catch (EmptyResultDataAccessException e) {
        return null;  // Return null if no employee is found
    } 
    }

    public Employee findByIdWithDepartment(Long id) {
        String sql = "SELECT e.*, d.dname as department_name " +
                     "FROM employees e " +
                     "JOIN department d ON e.dept_id = d.dept_id " +
                     "WHERE e.id = ?";
    
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
    }
    

    // Find employee by username
    public Employee findByUsername(String username) {
        String sql = "SELECT * FROM employees WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), username);
    }

    public boolean existsByUsername(String username) {
        String sql = "SELECT COUNT(*) FROM employees WHERE username = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, username);
        return count != null && count > 0;
    }

    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM employees WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    public Employee findByEmail(String email) {
        String sql = "SELECT * FROM employees WHERE email = ?";
        return jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), email);
    }

    public boolean existsByContactNumber(String contact_number) {
        String sql = "SELECT COUNT(*) FROM employees WHERE contact_number = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, contact_number);
        return count != null && count > 0;
    }
    
    // Insert new employee
    public void save(Employee employee) {
        String sql = "INSERT INTO employees (first_name, last_name, address, email, contact_number, dept_id, dob, username, password, gender, role, emergency_contact, position, bankAccountNumber) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            employee.getFirstName(), 
            employee.getLastName(), 
            employee.getAddress(), 
            employee.getEmail(), 
            employee.getContactNumber(),  // Contact number
            // employee.getSalary(), 
            employee.getDeptId(),  // Department ID
            employee.getDob(),  // Date of Birth
            employee.getUsername(),  // Username
            employee.getPassword(),  // Password
            employee.getGender(),  // Gender
            employee.getRole(),  // Role
            employee.getEmergencyContact(),  // Emergency Contact
            employee.getPosition(),
            employee.getBankAccountNumber()
        );
    }
    
    public void update(Employee employee) {
        String sql = employee.getreportsTo() != 0 ?
            "UPDATE employees SET first_name = ?, last_name = ?, address = ?, email = ?, contact_number = ?, dept_id = ?, dob = ?, username = ?, password = ?, gender = ?, role = ?, emergency_contact = ?, position = ?, reports_to = ?, position = ?, bankAccountNumber = ? WHERE id = ?" :
            "UPDATE employees SET first_name = ?, last_name = ?, address = ?, email = ?, contact_number = ?, dept_id = ?, dob = ?, username = ?, password = ?, gender = ?, role = ?, emergency_contact = ?, position = ?, reports_to = NULL, position = ?, bankAccountNumber = ? WHERE id = ?";
    
        if (employee.getreportsTo() != 0) {
            jdbcTemplate.update(sql,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAddress(),
                employee.getEmail(),
                employee.getContactNumber(),
                // employee.getSalary(),
                employee.getDeptId(),
                employee.getDob(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getGender(),
                employee.getRole(),
                employee.getEmergencyContact(),
                employee.getPosition(),
                employee.getreportsTo(),  // Set reportsTo if not null
                employee.getPosition(),
                employee.getBankAccountNumber(),
                employee.getId()
            );
        } else {
            jdbcTemplate.update(sql,
                employee.getFirstName(),
                employee.getLastName(),
                employee.getAddress(),
                employee.getEmail(),
                employee.getContactNumber(),
                // employee.getSalary(),
                employee.getDeptId(),
                employee.getDob(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getGender(),
                employee.getRole(),
                employee.getEmergencyContact(),
                employee.getPosition(),
                employee.getPosition(),
                employee.getBankAccountNumber(),
                employee.getId()  // Employee ID for WHERE clause
            );
        }
    }
    

    // Delete employee by ID
    public void deleteById(Long id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    // Check if employee exists
    public boolean existsById(Long id) {
        String sql = "SELECT COUNT(*) FROM employees WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }


    // RowMapper implementation
    private static class EmployeeRowMapperWithDept implements RowMapper<Employee> {
        @Override
        public Employee mapRow(ResultSet rs, int rowNum) throws SQLException {
            Employee employee = new Employee();
            employee.setId(rs.getLong("id"));
            employee.setFirstName(rs.getString("first_name"));
            employee.setLastName(rs.getString("last_name"));
            employee.setAddress(rs.getString("address"));
            employee.setEmail(rs.getString("email"));
            employee.setContactNumber(rs.getString("contact_number"));  // Contact Number
            // employee.setSalary(rs.getDouble("salary"));
            employee.setDeptId(rs.getInt("dept_id"));  // Department ID
            employee.setDob(rs.getDate("dob").toLocalDate());  // Date of Birth
            employee.setUsername(rs.getString("username"));  // Username
            employee.setPassword(rs.getString("password"));  // Password
            employee.setGender(rs.getString("gender"));  // Gender
            employee.setRole(rs.getString("role"));  // Role
            employee.setEmergencyContact(rs.getString("emergency_contact"));  // Emergency Contact
            employee.setreportsTo(rs.getLong("reports_to"));  // Emergency Contact
            employee.setDepartmentName(rs.getString("department_name"));
            employee.setPosition(rs.getString("position"));
            employee.setbankAccountNumber(rs.getString("bankAccountNumber"));
            // employee.setAge(rs.getInt("age"));  // Age
            return employee;
        }
    }

    // Add search functionality in EmployeeDAO
    public List<Employee> searchByName(String keyword) {
        String sql = "SELECT * FROM employees WHERE first_name LIKE ? OR last_name LIKE ?";
        String searchKeyword = "%" + keyword + "%";  // Adding wildcard for partial match
        return jdbcTemplate.query(sql, new EmployeeRowMapper(), searchKeyword, searchKeyword);
    }

    @SuppressWarnings("deprecation")
    public List<Employee> getEmployeesByManagerId(Long managerId) {
        String sql = "SELECT * FROM Employees WHERE reports_to = ?";
        return jdbcTemplate.query(sql, new Object[]{managerId}, new EmployeeRowMapper());
    }

    public List<Employee> findEmployeesByHod(long hodId) {
        String sql = "SELECT * FROM employees WHERE dept_id = (SELECT dept_id FROM department WHERE hod_id = ?)";
        return jdbcTemplate.query(sql, new Object[]{hodId}, new EmployeeRowMapper());
    }
    

}
