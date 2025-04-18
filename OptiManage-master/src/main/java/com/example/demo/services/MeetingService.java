package com.example.demo.services;

import com.example.demo.dao.MeetingDAO;
import com.example.demo.entity.Meeting;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class MeetingService {

    private final MeetingDAO meetingDAO;

    public MeetingService(MeetingDAO meetingDAO) {
        this.meetingDAO = meetingDAO;
    }

    // Schedule a new meeting
    public void scheduleMeeting(int clientId, Long relationshipManagerId, LocalDate meetingDate, LocalTime meetingTime) {
        LocalDateTime meetingDateTime = LocalDateTime.of(meetingDate, meetingTime);

        // Check if the meeting date and time is in the past
        if (meetingDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Cannot schedule a meeting for a past date and time.");
        }
        
        
        Meeting meeting = new Meeting();
        meeting.setClientId(clientId);
        meeting.setRelationshipManagerId(relationshipManagerId);
        meeting.setMeetingDate(meetingDate);
        meeting.setMeetingTime(meetingTime);
        meeting.setMeetingStatus("Scheduled");

        // Save the meeting in the database
        meetingDAO.save(meeting);

        // Notify the Relationship Manager
        notifyRelationshipManager(relationshipManagerId, meetingDate, meetingTime);
    }

    // Get all meetings for a client
    public List<Meeting> getClientMeetings(int clientId) {
        return meetingDAO.getMeetingsByClientId(clientId);
    }

    // Get all meetings for a Relationship Manager
    public List<Meeting> getRelationshipManagerMeetings(Long relationshipManagerId) {
        return meetingDAO.getMeetingsByRelationshipManagerId(relationshipManagerId);
    }

    // Notify the Relationship Manager of the scheduled meeting
    private void notifyRelationshipManager(Long relationshipManagerId, LocalDate meetingDate, LocalTime meetingTime) {
        // Logic to send notification (e.g., email, SMS, etc.)
        System.out.println("Notification sent to Relationship Manager (ID: " + relationshipManagerId +
                ") for meeting on " + meetingDate + " at " + meetingTime);
    }

    // Update the meeting status
    public void updateMeetingStatus(int meetingId, String status) {
        meetingDAO.updateMeetingStatus(meetingId, status);
    }

}
