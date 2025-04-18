package com.example.demo.dao;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeProject;
import com.example.demo.entity.Project;
import com.example.demo.rowmapper.EmployeeProjectRowMapper;
import com.example.demo.rowmapper.EmployeeRowMapper;
import com.example.demo.rowmapper.ProjectRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeProjectDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Method to add an employee to a project
    public void addEmployeeToProject(long empId, int projId) {
        String sql = "INSERT INTO employee_project (emp_id, proj_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, empId, projId);
    }

    // Method to remove an employee from a project
    @SuppressWarnings("deprecation")
    public void removeEmployeeFromProject(long empId, int projId) {
        // Check if the employee being removed is the leader of the project
        String checkLeadSql = "SELECT lead_by FROM project WHERE proj_id = ?";
        Integer leadByEmpId = jdbcTemplate.queryForObject(checkLeadSql, new Object[]{projId}, Integer.class);
    
        // If the employee being removed is the project leader, set lead_by to 0
        if (leadByEmpId != null && leadByEmpId == empId) {
            String updateLeadSql = "UPDATE project SET lead_by = 0 WHERE proj_id = ?";
            jdbcTemplate.update(updateLeadSql, projId);
        }
    
        // Now remove the employee from the employee_project table
        String sql = "DELETE FROM employee_project WHERE emp_id = ? AND proj_id = ?";
        jdbcTemplate.update(sql, empId, projId);
    }
    

    // Method to find all EmployeeProject records for a specific project
    @SuppressWarnings("deprecation")
    public List<EmployeeProject> findByProjectId(int projId) {
        String sql = "SELECT emp_id, proj_id FROM employee_project WHERE proj_id = ?";
        return jdbcTemplate.query(sql, new Object[]{projId}, new EmployeeProjectRowMapper());
    }

    // Method to find all EmployeeProject records for a specific employee
    @SuppressWarnings("deprecation")
    public List<EmployeeProject> findByEmployeeId(long empId) {
        String sql = "SELECT emp_id, proj_id FROM employee_project WHERE emp_id = ?";
        return jdbcTemplate.query(sql, new Object[]{empId}, new EmployeeProjectRowMapper());
    }

     public List<Project> findProjectsLedByEmployeeId(Long employeeId) {
        String sql = "SELECT * FROM Project WHERE lead_by = ?";
        return jdbcTemplate.query(sql, new ProjectRowMapper(), employeeId);
    }

    public Long getLeadIdByProjectId(long projectId) {
        String sql = "SELECT lead_by FROM Project WHERE proj_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{projectId}, Long.class);
    }

    

}