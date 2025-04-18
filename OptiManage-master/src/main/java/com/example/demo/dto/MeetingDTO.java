package com.example.demo.dto;

import java.time.LocalDateTime;

public class MeetingDTO {
    private int clientId;
    private int relationshipManagerId;
    private LocalDateTime meetingDateTime;

    // Getters and Setters
    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getRelationshipManagerId() {
        return relationshipManagerId;
    }

    public void setRelationshipManagerId(int relationshipManagerId) {
        this.relationshipManagerId = relationshipManagerId;
    }

    public LocalDateTime getMeetingDateTime() {
        return meetingDateTime;
    }

    public void setMeetingDateTime(LocalDateTime meetingDateTime) {
        this.meetingDateTime = meetingDateTime;
    }
}
