package com.example.demo.dao;

import com.example.demo.entity.Project;
import com.example.demo.rowmapper.ProjectRowMapper;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProjectDAO {
    private final JdbcTemplate jdbcTemplate;

    public ProjectDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Project> getAllProjects() {
        String sql = "SELECT * FROM Project";
        return jdbcTemplate.query(sql, new ProjectRowMapper());
    }

    @SuppressWarnings("deprecation")
    public Project getProjectById(int projId) {
        String sql = "SELECT * FROM Project WHERE proj_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{projId}, new ProjectRowMapper());
    }

public int addProject(Project project) {
    String sql = "INSERT INTO Project (pname, description, start_date, status, dept_id) VALUES (?, ?, ?, ?, ?)";

    Object startDate = project.getStartDate() == null ? null : project.getStartDate();

    return jdbcTemplate.update(sql, project.getPname(), project.getDescription(), startDate, project.getStatus(), project.getDeptId());
}


    public int updateProject(Project project) {
        String sql = "UPDATE Project SET pname = ?, description = ?, start_date = ?, end_date = ?, status = ?, dept_id = ?, lead_by=? WHERE proj_id = ?";
        return jdbcTemplate.update(sql, project.getPname(), project.getDescription(), project.getStartDate(), project.getEndDate(), project.getStatus(), project.getDeptId(),project.getLeadBy() ,project.getProjId());
    }

    public int deleteProject(int projId) {
        String sql = "DELETE FROM Project WHERE proj_id = ?";
        return jdbcTemplate.update(sql, projId);
    }
}