package com.example.demo.dao;

import java.util.List;
import com.example.demo.entity.Department;
import com.example.demo.rowmapper.DepartmentRowMapper;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDAO {

    private final JdbcTemplate jdbcTemplate;

    public DepartmentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Fetch all departments
    public List<Department> findAll() {
        String sql = "SELECT * FROM Department";
        return jdbcTemplate.query(sql, new DepartmentRowMapper());
    }

    // Insert new department
    public void save(Department department) {
        String sql = "INSERT INTO Department (dname, est_date, contact_detail, email, hod_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, 
            department.getDname(),
            department.getEstDate(),
            department.getContactDetail(),
            department.getEmail(),
            department.getHodId()  // HOD ID
        );
    }

    // Find department by ID
    public Department findById(int deptId) {
        String sql = "SELECT * FROM Department WHERE dept_id = ?";
        return jdbcTemplate.queryForObject(sql, new DepartmentRowMapper(), deptId);
    }

    // Update existing department
    public void update(Department department) {
        String sql = "UPDATE Department SET dname = ?, est_date = ?, contact_detail = ?, email = ?, hod_id = ? WHERE dept_id = ?";
        jdbcTemplate.update(sql, 
            department.getDname(),
            department.getEstDate(),
            department.getContactDetail(),
            department.getEmail(),
            department.getHodId(),
            department.getDeptId()
        );
    }

    // Delete department by ID
    public void deleteById(int deptId) {
        String sql = "DELETE FROM Department WHERE dept_id = ?";
        jdbcTemplate.update(sql, deptId);
    }
}
