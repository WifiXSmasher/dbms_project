package com.example.demo.rowmapper;

import com.example.demo.entity.EmployeeProject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeProjectRowMapper implements RowMapper<EmployeeProject> {

    @Override
    public EmployeeProject mapRow(ResultSet rs, int rowNum) throws SQLException {
        long empId = rs.getLong("emp_id");
        int projId = rs.getInt("proj_id");

        return new EmployeeProject(empId, projId); // Create and return EmployeeProject object
    }
}