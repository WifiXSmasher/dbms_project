package com.example.demo.controller;

import com.example.demo.dao.ClientDAO;
import com.example.demo.entity.Client;
import com.example.demo.entity.Meeting;
import com.example.demo.services.MeetingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MeetingController {

    private final MeetingService meetingService;

    @Autowired
    ClientDAO clientDAO;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // Show scheduled meetings for a particular Relationship Manager
    @GetMapping("/scheduledMeetings/{relationshipManagerId}")
    @PreAuthorize("hasAnyRole('ROLE_EMPLOYEE', 'ROLE_ADMIN')")
    public String showScheduledMeetings(@PathVariable("relationshipManagerId") Long relationshipManagerId,
            Model model) {
        List<Meeting> scheduledMeetings = meetingService.getRelationshipManagerMeetings(relationshipManagerId);
        model.addAttribute("scheduledMeetings", scheduledMeetings);
        return "scheduledMeetings"; // Return the view for the scheduled meetings page
    }

    // New Mapping for Client Dashboard
    
    @GetMapping("/clients/dashboard/{clientId}")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String clientDashboard(@PathVariable int clientId, Model model) {
        Client client = clientDAO.getClientById(clientId);
        model.addAttribute("client", client);

        // Add other necessary details for the dashboard like meetings
        List<Meeting> meetings = meetingService.getClientMeetings(clientId);
        model.addAttribute("meetings", meetings);

        return "client-dashboard"; // Return the client-dashboard.html view
    }

    // Mapping for scheduling a meeting (use MeetingService to schedule)
    @PostMapping("/clients/dashboard/{clientId}/scheduleMeeting")
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public String scheduleMeeting(@PathVariable int clientId,
            @RequestParam("relationshipManagerId") Long relationshipManagerId,
            @RequestParam("meetingDate") String meetingDate,
            @RequestParam("meetingTime") String meetingTime) {

        // Convert String date and time to LocalDate and LocalTime
        LocalDate date = LocalDate.parse(meetingDate);
        LocalTime time = LocalTime.parse(meetingTime);

        // Schedule the meeting using the MeetingService
        meetingService.scheduleMeeting(clientId, relationshipManagerId, date, time);

        // Redirect to the client dashboard after scheduling
        return "redirect:/clients/dashboard/" + clientId;
    }
}
