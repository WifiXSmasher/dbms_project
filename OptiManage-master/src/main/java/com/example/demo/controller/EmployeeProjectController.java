package com.example.demo.controller;

import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dao.EmployeeProjectDAO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.EmployeeProject;
import com.example.demo.entity.Project;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.stream.Collectors;

import java.util.List;

@Controller
public class EmployeeProjectController {

    @Autowired
    private EmployeeProjectDAO employeeProjectDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    // Updated method to use empId as a Path Variable
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/employee/projects")
    public String viewEmployeeProjects( Model model) {
        // Fetch the projects for the provided employee ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long employeeId = employee.getId();

        // List<EmployeeProject> projects = employeeProjectDAO.findByEmployeeId(employeeId);
        List<Project> leadProjects = employeeProjectDAO.findProjectsLedByEmployeeId(employeeId); 

        // Add the list of projects to the model
        // model.addAttribute("projects", projects);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("leadProjects", leadProjects);
        return "employeeProjects"; // Redirect to the Thymeleaf template 'employeeProjects.html'
    }

    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/employee/showprojects")
    public String onlyViewEmployeeProjects( Model model) {
        // Fetch the projects for the provided employee ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long employeeId = employee.getId();

        List<EmployeeProject> projects = employeeProjectDAO.findByEmployeeId(employeeId);
        List<Project> leadProjects = employeeProjectDAO.findProjectsLedByEmployeeId(employeeId); 

        // Add the list of projects to the model
        model.addAttribute("projects", projects);
        model.addAttribute("employeeId", employeeId);
        model.addAttribute("leadProjects", leadProjects);
        return "onlyemployeeProjects"; // Redirect to the Thymeleaf template 'employeeProjects.html'
    }
   // Assuming you have a method in EmployeeDAO to retrieve employees by project ID directly:
   @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
   @GetMapping("projects/employees/{projectId}")
   public String showEmployeesByProject(@PathVariable int projectId, Model model) {
       // Step 1: Fetch the list of EmployeeProject entries for the specified project ID
       List<EmployeeProject> employeeProjects = employeeProjectDAO.findByProjectId(projectId);
   
       // Step 2: Use empId from each EmployeeProject to fetch the full Employee information
       List<Employee> employees = employeeProjects.stream()
               .map(empProj -> employeeDAO.findById(empProj.getEmpId())) // Retrieve full Employee details
               .collect(Collectors.toList());
       Long leadId = employeeProjectDAO.getLeadIdByProjectId(projectId);
       // Step 3: Add the list of full Employee details and project ID to the model
       model.addAttribute("employees", employees);
       model.addAttribute("projectId", projectId); // Pass project ID to the view
       model.addAttribute("leadId", leadId);

       return "project-employees"; // View name where the data will be displayed
   }  
   
   


}