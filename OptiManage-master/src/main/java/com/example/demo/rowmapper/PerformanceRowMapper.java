package com.example.demo.rowmapper;

import com.example.demo.entity.Performance;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PerformanceRowMapper implements RowMapper<Performance> {
    @Override
    public Performance mapRow(ResultSet rs, int rowNum) throws SQLException {
        Performance performance = new Performance();
        performance.setEvalId(rs.getInt("eval_id"));
        performance.setReviewee(rs.getLong("reviewee"));
        performance.setReviewer(rs.getLong("reviewer"));
        performance.setRating(rs.getInt("rating"));
        performance.setFeedback(rs.getString("feedback"));
        performance.setDate(rs.getDate("date").toLocalDate());
        performance.setProjectId(rs.getInt("project_id")); // Set projectId from the ResultSet
        return performance;
    }
}
