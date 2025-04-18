package com.example.demo.controller;

import com.example.demo.entity.Project;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Employee;
import com.example.demo.dao.ProjectDAO;
import com.example.demo.dao.EmployeeDAO;

import com.example.demo.entity.EmployeeProject; // Import the new entity
import com.example.demo.dao.EmployeeProjectDAO; // Import the new DAO

import java.util.stream.Collectors;


import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/projects")
public class ProjectController {
    @Autowired
    private ProjectDAO projectDAO;
    @Autowired
    private EmployeeDAO employeeDAO;
    @Autowired
    private EmployeeProjectDAO employeeProjectDAO; // Add this line


    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping
    public String getAllProjects(Model model) {
        System.out.println("Fetching all projects..."); // Simple log statement
        System.out.println("Fetching all projects..."); // Simple log statement
        List<Project> projects = projectDAO.getAllProjects();
        model.addAttribute("projects", projects);
        return "projects";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/assign_lead/{projectId}")
    public String showAssignLeadPage(@PathVariable int projectId, Model model) {
        Project project = projectDAO.getProjectById(projectId);
        List<Employee> employees = employeeDAO.findAll(); // Fetch all employees
        model.addAttribute("project", project);
        model.addAttribute("employees", employees);
        return "project/assign_lead";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/assign_lead")
    public String assignLead(@RequestParam int projectId, @RequestParam Long empId, RedirectAttributes redirectAttributes) {
        Project project = projectDAO.getProjectById(projectId);
        System.out.println("Lead assigned: " + empId);

        project.setLeadBy(empId);
        projectDAO.updateProject(project);
        // return "redirect:/projects";
        return "redirect:/projects/assign_lead/" + projectId;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/add")
    public String showAddProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "addproject";
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/add")
    public String addProject(@ModelAttribute Project project) {
        projectDAO.addProject(project);
        return "redirect:/projects";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/edit/{id}")
    public String showEditProjectForm(@PathVariable int id, Model model) {
        Project project = projectDAO.getProjectById(id);
        model.addAttribute("project", project);
        return "editProjects";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/edit")
    public String editProject(@ModelAttribute Project project) {
        projectDAO.updateProject(project);
        return "redirect:/projects";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/delete/{id}")
    public String deleteProject(@PathVariable int id) {
        projectDAO.deleteProject(id);
        return "redirect:/projects";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/manage_employees/{projectId}")
    public String manageEmployees(@PathVariable int projectId, Model model) {
        Project project = projectDAO.getProjectById(projectId);
        
        // Get all employees
        List<Employee> allEmployees = employeeDAO.findAll();
        
        // Get assigned employees from the employee_project table (only empIds are present here)
        List<EmployeeProject> assignedEmployeeProjects = employeeProjectDAO.findByProjectId(projectId);

        // List of assigned employees for the project (including the leader)
        List<Employee> assignedEmployees = assignedEmployeeProjects.stream()
            .map(empProj -> employeeDAO.findById(empProj.getEmpId())) // Assuming employeeDAO has a findById method
            .collect(Collectors.toList());

        // Check if project has a leader
        Employee leadByEmployee = employeeDAO.findById(project.getLeadBy()); // Assuming project has getLeadByEmpId()

        if (leadByEmployee != null) {
            assignedEmployees.add(leadByEmployee); // Ensure leader is in the assigned employees
        }

        // Find available employees (those not assigned to the project and not the leader)
        List<Employee> availableEmployees = allEmployees.stream()
            .filter(emp -> assignedEmployees.stream().noneMatch(e -> e.getId() == emp.getId())) // Filter out assigned employees
            .filter(emp -> emp.getId() != project.getLeadBy()) // Exclude the project leader from available employees
            .collect(Collectors.toList());

    
        // Add attributes to the model
        model.addAttribute("project", project);
        model.addAttribute("currentEmployees", assignedEmployees);
        model.addAttribute("availableEmployees", availableEmployees);
    
        return "project/manage_employees"; // Update to the correct view name
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/add_employee")
    public String addEmployeeToProject(@RequestParam("projectId") int projectId, 
                                       @RequestParam("empId") long empId) {
        // Create a new EmployeeProject object using the provided projectId and empId
        // EmployeeProject employeeProject = new EmployeeProject(empId, projectId);

        // Save the association to the database via DAO
        employeeProjectDAO.addEmployeeToProject(empId,projectId);

        // Redirect back to the project management page (adjust as necessary)
        return "redirect:/projects/manage_employees/" + projectId;
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/remove_employee")
    public String removeEmployeeFromProject(@RequestParam("projectId") int projectId, 
                                       @RequestParam("empId") long empId) {
        // Create a new EmployeeProject object using the provided projectId and empId
        // EmployeeProject employeeProject = new EmployeeProject(empId, projectId);

        // Save the association to the database via DAO
        employeeProjectDAO.removeEmployeeFromProject(empId,projectId);

        // Redirect back to the project management page (adjust as necessary)
        return "redirect:/projects/manage_employees/" + projectId;
    }
    
}
