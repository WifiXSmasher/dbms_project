package com.example.demo.rowmapper;

import com.example.demo.entity.Project;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProjectRowMapper implements RowMapper<Project> {
    @Override
    public Project mapRow(ResultSet rs, int rowNum) throws SQLException {
        Project project = new Project();
        project.setProjId(rs.getInt("proj_id"));
        project.setPname(rs.getString("pname"));
        project.setDescription(rs.getString("description"));
        project.setStartDate(rs.getDate("start_date"));
        project.setEndDate(rs.getDate("end_date"));
        project.setStatus(rs.getString("status"));
        project.setDeptId(rs.getInt("dept_id"));
        project.setLeadBy(rs.getLong("lead_by"));
        project.setDeadline(rs.getDate("deadline"));
        return project;
    }
}