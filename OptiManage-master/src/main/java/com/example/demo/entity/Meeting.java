package com.example.demo.entity;

import java.time.LocalDate;
import java.time.LocalTime;

public class Meeting {

    private int meetingId;
    private int clientId;
    private Long relationshipManagerId;
    private LocalDate meetingDate;
    private LocalTime meetingTime;
    private String meetingStatus;

    // Getters and Setters

    public int getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(int meetingId) {
        this.meetingId = meetingId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public Long getRelationshipManagerId() {
        return relationshipManagerId;
    }

    public void setRelationshipManagerId(Long relationshipManagerId) {
        this.relationshipManagerId =  relationshipManagerId;
    }

    public LocalDate getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(LocalDate meetingDate) {
        this.meetingDate = meetingDate;
    }

    public LocalTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingStatus() {
        return meetingStatus;
    }

    public void setMeetingStatus(String meetingStatus) {
        this.meetingStatus = meetingStatus;
    }
}
