package com.example.demo.controller;

import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dao.SalaryDAO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Salary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.List;

@Controller
public class SalaryController {

    @Autowired
    private SalaryDAO salaryDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    // Show salary slip for an employee
    @GetMapping("/employee/{id}/salary-slip")
    public String showSalarySlip(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeDAO.findById(id);  // Retrieve employee details
        Salary salary = salaryDAO.findByEmpId(id);     // Retrieve salary details
        model.addAttribute("employee", employee);
        model.addAttribute("salary", salary);
        return "salary-slip-emp"; // Refers to salary-slip.html template
    }

    @GetMapping("/salaries")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllSalaries(Model model) {
        List<Salary> salaries = salaryDAO.getAllSalaries();
        model.addAttribute("salaries", salaries);
        return "salaries"; // Thymeleaf template name
    }

    // Show form to add a new salary
    @GetMapping("salary/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddSalaryForm(Model model) {
        model.addAttribute("salary", new Salary());
        return "addsalary"; // Thymeleaf template name
    }

    // Handle form submission to add a new salary
    @PostMapping("salary/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addSalary(@ModelAttribute Salary salary) {
        salaryDAO.addSalary(salary);
        return "redirect:/salaries";
    }

    // Show form to edit an existing salary
    @GetMapping("salary/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditSalaryForm(@PathVariable int id, Model model) {
        Salary salary = salaryDAO.getSalaryById(id);
        model.addAttribute("salary", salary);
        return "editsalary"; // Thymeleaf template name
    }

    // Handle form submission to update a salary
    @PostMapping("salary/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editSalary(@ModelAttribute Salary salary) {
        salaryDAO.updateSalary(salary);
        return "redirect:/salaries";
    }

    // Delete a salary
    @GetMapping("salary/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteSalary(@PathVariable int id) {
        salaryDAO.deleteSalary(id);
        return "redirect:/salaries";
    }
}

