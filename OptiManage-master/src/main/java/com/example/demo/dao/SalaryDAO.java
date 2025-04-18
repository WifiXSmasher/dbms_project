// SalaryDAO.java
package com.example.demo.dao;

// import com.example.demo.entity.Employee;
import com.example.demo.entity.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class SalaryDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Retrieve Salary by Employee ID
    public Salary findByEmpId(Long empId) {
        String sql = "SELECT * FROM salary WHERE emp_id = ?";
        return jdbcTemplate.queryForObject(sql, new SalaryRowMapper(), empId);
    }

    // Calculate salary slip based on working and absent days
    public Salary calculateSalarySlip(Long empId, int workingDays, int absentDays) {
        // Retrieve existing salary details
        Salary salary = findByEmpId(empId);

        double baseSalary = salary.getBaseSalary();
        int totalDays = workingDays + absentDays;
        double dailySalary = baseSalary / totalDays;

        // Get the number of leave days taken in the current month
        int leaveDaysTaken = getLeaveDaysForCurrentMonth(empId);

        // Calculate salary after excluding the leave days from the absent days
        int effectiveAbsentDays = absentDays - leaveDaysTaken;

        if (effectiveAbsentDays < 0) {
            effectiveAbsentDays = 0; // Prevent negative absent days
        }

        // Calculate the salary for the working days
        double calculatedSalary = dailySalary * workingDays;

        // Adjust salary based on allowances and deductions
        double hra = (salary.getHra() / 100) * calculatedSalary;
        double medicalAllowance = salary.getMedicalAllowance();
        double travelAllowance = salary.getTravelAllowance();
        double pfDeduction = (salary.getPfDeduction() / 100) * calculatedSalary;

        // Deduct salary based on the remaining absent days
        double absentDeduction = dailySalary * effectiveAbsentDays;

        // Update salary fields
        salary.setBaseSalary(calculatedSalary - absentDeduction); // Deduct salary for absent days
        salary.setHra(hra);
        salary.setMedicalAllowance(medicalAllowance);
        salary.setTravelAllowance(travelAllowance);
        salary.setPfDeduction(pfDeduction);

        return salary;
    }

public int getLeaveDaysForCurrentMonth(Long empId) {
    LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1); // Start of the current month
    LocalDate endOfMonth = LocalDate.now().withDayOfMonth(startOfMonth.lengthOfMonth()); // End of the current month

    String sql = "SELECT SUM(DATEDIFF(end_date, start_date) + 1) " +
                 "FROM Leaves WHERE emp_id = ? AND status = 'Approved' " + // Consider only approved leaves
                 "AND start_date BETWEEN ? AND ?";
    
    return jdbcTemplate.queryForObject(sql, Integer.class, empId, Date.valueOf(startOfMonth), Date.valueOf(endOfMonth));
}

// Retrieve all salaries
public List<Salary> getAllSalaries() {
    String sql = "SELECT * FROM Salary";
    return jdbcTemplate.query(sql, new SalaryRowMapper());
}

// Retrieve a single salary by ID
public Salary getSalaryById(int id) {
    String sql = "SELECT * FROM Salary WHERE id = ?";
    return jdbcTemplate.queryForObject(sql, new Object[]{id}, new SalaryRowMapper());
}

// Add a new salary
public int addSalary(Salary salary) {
    String sql = "INSERT INTO Salary (emp_id, base_salary, hra, medical_allowance, travel_allowance, pf_deduction, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    return jdbcTemplate.update(sql, salary.getEmpId(), salary.getBaseSalary(), salary.getHra(), salary.getMedicalAllowance(), salary.getTravelAllowance(), salary.getPfDeduction(), salary.getStartDate(), salary.getEndDate());
}

// Update an existing salary
public int updateSalary(Salary salary) {
    String sql = "UPDATE Salary SET emp_id = ?, base_salary = ?, hra = ?, medical_allowance = ?, travel_allowance = ?, pf_deduction = ?, start_date = ?, end_date = ? WHERE id = ?";
    return jdbcTemplate.update(sql, salary.getEmpId(), salary.getBaseSalary(), salary.getHra(), salary.getMedicalAllowance(), salary.getTravelAllowance(), salary.getPfDeduction(), salary.getStartDate(), salary.getEndDate(), salary.getId());
}

// Delete a salary by ID
public int deleteSalary(int id) {
    String sql = "DELETE FROM Salary WHERE id = ?";
    return jdbcTemplate.update(sql, id);
}


    // Inner class for mapping rows of the result set to Salary entity
    private static class SalaryRowMapper implements RowMapper<Salary> {
        @Override
        public Salary mapRow(ResultSet rs, int rowNum) throws SQLException {
            Salary salary = new Salary();
            salary.setId(rs.getLong("id"));
            salary.setEmpId(rs.getLong("emp_id"));
            salary.setBaseSalary(rs.getDouble("base_salary"));
            salary.setHra(rs.getDouble("hra"));
            salary.setMedicalAllowance(rs.getDouble("medical_allowance"));
            salary.setTravelAllowance(rs.getDouble("travel_allowance"));
            salary.setPfDeduction(rs.getDouble("pf_deduction"));
            
            // Convert SQL Date to LocalDate for start_date and end_date
            Date sqlStartDate = rs.getDate("start_date");
            Date sqlEndDate = rs.getDate("end_date");
            if (sqlStartDate != null) {
                salary.setStartDate(sqlStartDate.toLocalDate());
            }
            if (sqlEndDate != null) {
                salary.setEndDate(sqlEndDate.toLocalDate());
            }
    
            return salary;
        }
    }

    

public void save(Salary salary) {
    String sql = "UPDATE Salary SET base_salary = ?, hra = ?, medical_allowance = ?, travel_allowance = ?, pf_deduction = ?, start_date = ?, end_date = ? "
               + "WHERE emp_id = ?";
    
    // Calculate the first date of the current month
    LocalDate startDate = LocalDate.now().withDayOfMonth(1);
    
    // Calculate the last date of the current month
    LocalDate endDate = LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth());

    jdbcTemplate.update(sql,
        salary.getBaseSalary(),       // Base Salary
        salary.getHra(),              // HRA
        salary.getMedicalAllowance(), // Medical Allowance
        salary.getTravelAllowance(),  // Travel Allowance
        salary.getPfDeduction(),      // PF Deduction
        startDate,                    // Start Date as String
        endDate,                      // End Date as String
        // salary.getStartDate(),
        // salary.getEndDate(),
        salary.getEmpId()             // Employee ID (used for finding the record)
    );
}

}

