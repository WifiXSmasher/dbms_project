package com.example.demo.controller;

import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dao.PerformanceDAO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Performance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
public class PerformanceController {

    @Autowired
    private PerformanceDAO performanceDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/performance/{revieweeId}/review")
    public String showPerformanceForm(@PathVariable("revieweeId") long revieweeId,
                                      @RequestParam("reviewerId") long reviewerId,
                                      @RequestParam("projectId") int projectId,
                                      Model model) {
        // Check if performance entry exists
        
        Performance performance = performanceDAO.findPerformanceByRevieweeAndReviewer(revieweeId, reviewerId)
                .orElseGet(() -> {
                    // Create a new performance object if none exists
                    Performance newPerformance = new Performance();
                    newPerformance.setReviewee(revieweeId);
                    newPerformance.setReviewer(reviewerId);
                    System.out.println("Performance object in Controller: " + newPerformance.getProjectId());
                    newPerformance.setProjectId(projectId);
                    System.out.println("Performance object in Controller: " + newPerformance.getProjectId());
                    newPerformance.setDate(LocalDate.now());
                    return newPerformance;
                });

       

        model.addAttribute("performance", performance);
        model.addAttribute("projectId", projectId);  // Pass projectId for redirection after saving
        return "performance-form";
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/performance/save")
    public String savePerformance(@ModelAttribute("performance") Performance performance,
                                  @RequestParam("projectId") int projectId) {
        performanceDAO.saveOrUpdatePerformance(performance);
        return "redirect:/projects/employees/" + projectId;  // Redirect back to the employee list for the project
    }

    // Show employees in the HOD's department
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/departments/employees")
    public String showEmployeesByDepartment(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long id = employee.getId();

        List<Employee> employees = employeeDAO.findEmployeesByHod(id);
        model.addAttribute("employees", employees);
        return "department-employees";
    }

    @PreAuthorize("hasRole('ROLE_HOD')")
    @GetMapping("/employee/{id}/performance")
public String showEmployeePerformance(@PathVariable("id") Long employeeId, Model model) {
    List<Performance> performances = performanceDAO.findPerformanceByEmployeeId(employeeId);
    model.addAttribute("performances", performances);
    return "employee-performance";
}

@PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/performance/view")
    public String viewPerformance(Model model) {
        // Get the logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Fetch the employee based on the username
        Employee employee = employeeDAO.findByUsername(username);
        
        // Fetch the performance where the employee is the reviewee
        List<Performance> performances = performanceDAO.findPerformanceByReviewee(employee.getId());
        
        // Add the list of performances to the model
        model.addAttribute("performances", performances);
        
        return "view-performance"; // Return the view name that displays the performances
    }
}