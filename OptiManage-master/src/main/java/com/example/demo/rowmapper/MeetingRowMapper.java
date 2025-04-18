package com.example.demo.rowmapper;

import com.example.demo.entity.Meeting;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class MeetingRowMapper implements RowMapper<Meeting> {

    @Override
    public Meeting mapRow(ResultSet rs, int rowNum) throws SQLException {
        Meeting meeting = new Meeting();
        
        // Mapping the result set columns to the Meeting entity fields
        meeting.setMeetingId(rs.getInt("meeting_id"));
        meeting.setClientId(rs.getInt("client_id"));
        meeting.setRelationshipManagerId(rs.getLong("relationship_manager_id"));
        
        // Convert SQL Date and Time to Java LocalDate and LocalTime
        LocalDate meetingDate = rs.getDate("meeting_date").toLocalDate();
        LocalTime meetingTime = rs.getTime("meeting_time").toLocalTime();
        
        meeting.setMeetingDate(meetingDate);
        meeting.setMeetingTime(meetingTime);
        
        meeting.setMeetingStatus(rs.getString("meeting_status"));
        
        return meeting;
    }
}
