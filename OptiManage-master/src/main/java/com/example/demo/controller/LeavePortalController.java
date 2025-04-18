package com.example.demo.controller;


// import com.example.demo.service.UserService; // Ensure this import is correct

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dao.LeaveRequestDAO;
import com.example.demo.dao.NotificationDAO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.LeaveRequest;
import com.example.demo.entity.Notification;


@Controller 
@RequestMapping("/leave")
public class LeavePortalController {
    Long manager_emp=10L;
    Long EMPLOYEE_ID=14L;

    private static final Logger logger = LoggerFactory.getLogger(LeavePortalController.class);

    @Autowired
    private LeaveRequestDAO leaveRequestDAO;
    @Autowired
    private EmployeeDAO employeeDAO;
    @Autowired
    private NotificationDAO notificationDAO;

    // @Autowired
    // private UserService userService; // Assuming you have a UserService class

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/request/all")
    public String getAllLeaveRequests(Model model) {
        List<LeaveRequest> requests = leaveRequestDAO.getAllLeaveRequests();
        model.addAttribute("requests", requests); // Add requests to the model
        return "leaves/allLeaveRequests"; // Return the name of the HTML view
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/request/all2")
    public String getAllLeaveRequests2(Model model) {
        // Long managerId = (Long) session.getAttribute("empId"); // Get the logged-in manager's ID
        // Check if the manager has employees
        // List<Employee> employees = employeeDAO.getEmployeesByManagerId(managerId);
        // if (employees.isEmpty()) {
        //     model.addAttribute("message", "You have no employees reporting to you.");
        //     return "leaves/noEmployees"; // Return a view that indicates no employees
        // }
        // Get the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long managerId = employee.getId();
    
        // Fetch leave requests for the employees reporting to this manager
        List<LeaveRequest> requests = leaveRequestDAO.getLeaveRequestsByManagerId(managerId);
        model.addAttribute("requests", requests); // Add requests to the model
        model.addAttribute("employeeId", managerId); // Add requests to the model
        return "leaves/allLeaveRequests2"; // Return the name of the HTML view
    }
    

    // Add a leave request (REST API)
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    // @PostMapping("/request")
    // public String addLeaveRequest(
    //                               @ModelAttribute LeaveRequest leaveRequest,
    //                               RedirectAttributes redirectAttributes) {
    
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     String username = authentication.getName();

    //     // Fetch the employee details by username
    //     Employee employee = employeeDAO.findByUsername(username);
    //     Long employeeId = employee.getId();
    //     // Set the employee ID on the leave request
    //     leaveRequest.setEmpId(employeeId);
    //     leaveRequestDAO.addLeaveRequest(leaveRequest);
        
    //     // Add success message to redirect attributes
    //     redirectAttributes.addFlashAttribute("successMessage", "Leave request added successfully.");
    
    //     // Redirect to the requests view
    //     return "redirect:/leave/requests" ;
    // }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/request")
    public String addLeaveRequest(
    @ModelAttribute LeaveRequest leaveRequest,
    RedirectAttributes redirectAttributes) {

// Set the employee ID on the leave request
Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long employeeId = employee.getId();
leaveRequest.setEmpId(employeeId);
leaveRequestDAO.addLeaveRequest(leaveRequest);

// Add success message to redirect attributes
redirectAttributes.addFlashAttribute("successMessage", "Leave request added successfully.");

// Employee employee=employeeDAO.findById(employeeId);
Long managerId=employee.getreportsTo();
String notificationMessage = "Leave request for Employee ID " + employeeId + " has been added " ;
Notification notification = new Notification(managerId, notificationMessage); // Create the notification

// Save the notification
notificationDAO.createNotification(notification); // Assume notificationDAO is autowired


// Redirect to the requests view
return "redirect:/leave/requests";
}

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/requests")
    public String getLeaveRequestsByEmployeeId(Model model) {
        // Get the username from Authentication
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // String username = authentication.getName();
        // // Log the username for debugging
        // System.out.println("Loggggged in username: " + username);  // Print the username
    
        // Use the username to retrieve the employee ID from the database
        // Employee req_employee = employeeDAO.findByUsername(username); // Method in UserService to get employeeId by username
        // if (req_employee == null) {
        //     logger.error("No employee found for username: {}", username);
        //     model.addAttribute("error", "No employee found. Please check your username.");
        //     return "error"; // Return an appropriate error view
        // }
    
        // Long employeeId=req_employee.getId();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long employeeId = employee.getId();

        List<LeaveRequest> requests = leaveRequestDAO.getLeaveRequestsByEmployeeId(employeeId);
        model.addAttribute("requests", requests);
        model.addAttribute("employeeId", employeeId);
        return "leaves/myLeaves";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")

    @GetMapping("/request/add")
    public String addLeave(Model model) {
        logger.info("Accessed addLeave method");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long employeeId = employee.getId();
        
        LeaveRequest leaveRequest = new LeaveRequest();
        int paidLeaveCount = leaveRequestDAO.countByEmployeeIdAndLeaveType(employeeId, "Paid");

        // Determine if the employee has used up their 20 paid leaves
        boolean paidLeavesDone = paidLeaveCount >= 20;
    
        // Add attributes to the model
        model.addAttribute("leaveRequest", leaveRequest);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("paidLeavesDone", paidLeavesDone);
        model.addAttribute("paidLeaveCount", paidLeaveCount);

        return "leaves/add_leave_request"; // This should match the HTML file name
    }
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    // @ResponseBody
    // @PostMapping("/request/{empId}/{leaveType}/{startDate}")
    // public ResponseEntity<String> updateLeaveRequestStatus(
    //         @PathVariable Long empId,
    //         @PathVariable String leaveType,
    //         @PathVariable String startDate,
    //         @RequestParam String status) {
    
    //     LocalDate startDateParsed = LocalDate.parse(startDate); 
    //     leaveRequestDAO.updateLeaveRequestStatus(empId, leaveType, startDateParsed, status);
        
    //     // Return a success message in the response body
    //     return ResponseEntity.ok("Leave request updated successfully.");
    // }

    @ResponseBody
    @PostMapping("/request/{empId}/{leaveType}/{startDate}")
    public ResponseEntity<String> updateLeaveRequestStatus(
            @PathVariable Long empId,
            @PathVariable String leaveType,
            @PathVariable String startDate,
            @RequestParam String status) {
                
            // Employee employee=employeeDAO.findById(empId);
            // Long managerId=employee.getreportsTo();

        LocalDate startDateParsed = LocalDate.parse(startDate); 
        leaveRequestDAO.updateLeaveRequestStatus(empId, leaveType, startDateParsed, status);

        String notificationMessage = "Leave request for Employee ID " + empId + " has been updated to " + status + ".";
        Notification notification = new Notification(empId, notificationMessage); // Create the notification
    
        // Save the notification
        notificationDAO.createNotification(notification); // Assume notificationDAO is autowired
        
        // Return a success message in the response body
        return ResponseEntity.ok("Leave request updated successfully.");
    }
    

    // Delete a leave request (REST API)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @ResponseBody // Use @ResponseBody for REST API responses
    @DeleteMapping("/request/{leaveType}/{startDate}")
    public ResponseEntity<Void> deleteLeaveRequest(
            @PathVariable String leaveType,
            @PathVariable String startDate) {

        LocalDate startDateParsed = LocalDate.parse(startDate); // Parse to LocalDate
        leaveRequestDAO.deleteLeaveRequest(EMPLOYEE_ID, leaveType, startDateParsed);
        return ResponseEntity.ok().build();
    }
}