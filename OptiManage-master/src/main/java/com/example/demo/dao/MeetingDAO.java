package com.example.demo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Meeting;
import com.example.demo.rowmapper.MeetingRowMapper;

import java.util.List;

@Repository
public class MeetingDAO {
    
    private final JdbcTemplate jdbcTemplate;

    public MeetingDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Add a new meeting
    public int scheduleMeeting(Meeting meeting) {
        String sql = "INSERT INTO Meeting (client_id, relationship_manager_id, meeting_date, meeting_time, meeting_status) VALUES (?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
            meeting.getClientId(), 
            meeting.getRelationshipManagerId(), 
            meeting.getMeetingDate(), 
            meeting.getMeetingTime(), 
            meeting.getMeetingStatus());
    }

    // Save a new meeting
    public void save(Meeting meeting) {
        String sql = "INSERT INTO Meeting (client_id, relationship_manager_id, meeting_date, meeting_time, meeting_status) " +
                     "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, meeting.getClientId(), meeting.getRelationshipManagerId(), meeting.getMeetingDate(), meeting.getMeetingTime(), meeting.getMeetingStatus());
    }

    // Get all meetings for a client
    public List<Meeting> getMeetingsByClientId(int clientId) {
        String sql = "SELECT * FROM Meeting WHERE client_id = ?";
        return jdbcTemplate.query(sql, new MeetingRowMapper(), clientId);
    }

    // Get all meetings for a relationship manager
    public List<Meeting> getMeetingsByRelationshipManagerId(Long managerId) {
        String sql = "SELECT * FROM Meeting WHERE relationship_manager_id = ?";
        return jdbcTemplate.query(sql, new MeetingRowMapper(), managerId);
    }

    // Get a meeting by ID
    @SuppressWarnings("deprecation")
    public Meeting getMeetingById(int meetingId) {
        String sql = "SELECT * FROM Meeting WHERE meeting_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{meetingId}, new MeetingRowMapper());
    }

    // Update a meeting
    public int updateMeeting(Meeting meeting) {
        String sql = "UPDATE Meeting SET client_id = ?, relationship_manager_id = ?, meeting_date = ?, meeting_time = ?, meeting_status = ? WHERE meeting_id = ?";
        return jdbcTemplate.update(sql, 
            meeting.getClientId(), 
            meeting.getRelationshipManagerId(), 
            meeting.getMeetingDate(), 
            meeting.getMeetingTime(), 
            meeting.getMeetingStatus(),
            meeting.getMeetingId());
    }

    // Update meeting status (e.g., 'Completed', 'Cancelled', etc.)
    public void updateMeetingStatus(int meetingId, String status) {
        String sql = "UPDATE Meeting SET meeting_status = ? WHERE meeting_id = ?";
        jdbcTemplate.update(sql, status, meetingId);
    }

    // Delete a meeting
    public int deleteMeeting(int meetingId) {
        String sql = "DELETE FROM Meeting WHERE meeting_id = ?";
        return jdbcTemplate.update(sql, meetingId);
    }
}
