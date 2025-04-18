package com.example.demo.dao;

import com.example.demo.entity.Performance;
import com.example.demo.rowmapper.PerformanceRowMapper;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

@Repository
public class PerformanceDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Performance findById(int evalId) {
        String sql = "SELECT * FROM Performance WHERE eval_id = ?";
        return jdbcTemplate.queryForObject(sql, new PerformanceRowMapper(), evalId);
    }

public Optional<Performance> findPerformanceByRevieweeAndReviewer(long revieweeId, long reviewerId) {
        String sql = "SELECT * FROM Performance WHERE reviewee = ? AND reviewer = ?";
        try {
            Performance performance = jdbcTemplate.queryForObject(sql, new PerformanceRowMapper(), revieweeId, reviewerId);
            return Optional.ofNullable(performance);
        } catch (Exception e) {
            return Optional.empty();  // Return empty if no record is found
        }
    }

    // Save a new performance entry
    public void savePerformance(Performance performance) {
        String sql = "INSERT INTO Performance (reviewee, reviewer, rating, feedback, date, project_id) VALUES (?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, performance.getReviewee(), performance.getReviewer(),
                performance.getRating(), performance.getFeedback(), performance.getDate(), performance.getProjectId());
    }
    

    // Update an existing performance entry
    public void updatePerformance(Performance performance) {
        String sql = "UPDATE Performance SET rating = ?, feedback = ?, date = ?, project_id = ? WHERE eval_id = ?";
        jdbcTemplate.update(sql, performance.getRating(), performance.getFeedback(), performance.getDate(), performance.getProjectId(), performance.getEvalId());
    }
    

    // Save or update based on existence
    public void saveOrUpdatePerformance(Performance performance) {
        Optional<Performance> existingPerformance = findPerformanceByRevieweeAndReviewer(performance.getReviewee(), performance.getReviewer());

        if (existingPerformance.isPresent()) {
            // Update the existing performance record
            Performance existing = existingPerformance.get();
            existing.setRating(performance.getRating());
            existing.setFeedback(performance.getFeedback());
            existing.setDate(LocalDate.now());  // Update the date to current

            updatePerformance(existing);
        } else {
            // Insert a new performance record
            savePerformance(performance);
        }
    }

    public List<Performance> findPerformanceByEmployeeId(long employeeId) {
        String sql = "SELECT * FROM Performance WHERE reviewee = ?";
        return jdbcTemplate.query(sql, new Object[]{employeeId}, new PerformanceRowMapper());
    }

    // Method to fetch all performances where the employee is the reviewee
    public List<Performance> findPerformanceByReviewee(long revieweeId) {
        String sql = "SELECT * FROM Performance WHERE reviewee = ?";
        return jdbcTemplate.query(sql, new Object[]{revieweeId}, new PerformanceRowMapper());
    }


}