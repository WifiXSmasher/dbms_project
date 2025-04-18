package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.demo.dao.DepartmentDAO;
import com.example.demo.dao.EmployeeDAO;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;

import java.util.List;

@Controller
public class DepartmentController {

    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private EmployeeDAO employeeDAO;

    @GetMapping("/departments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listDepartments(Model model) {
        List<Department> departments = departmentDAO.findAll();
        model.addAttribute("departments", departments);
        return "departments";
    }

    @GetMapping("/departments/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddForm(Model model) {
        model.addAttribute("department", new Department());

        List<Employee> employees = employeeDAO.findAll();
        model.addAttribute("employees", employees);

        return "adddept";
    }

    @PostMapping("/departments/save")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String saveDepartment(@ModelAttribute Department department) {
        departmentDAO.save(department);
        return "redirect:/departments";
    }

    @GetMapping("/departments/edit/{deptId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditForm(@PathVariable int deptId, Model model) {
        Department department = departmentDAO.findById(deptId);
        model.addAttribute("department", department);

        List<Employee> employees = employeeDAO.findAll();
        model.addAttribute("employees", employees);

        return "editdept";
    }

    @PostMapping("/departments/update")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateDepartment(@ModelAttribute Department department) {
        departmentDAO.update(department);
        return "redirect:/departments";
    }

    @GetMapping("/departments/delete/{deptId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteDepartment(@PathVariable int deptId) {
        departmentDAO.deleteById(deptId);
        return "redirect:/departments";
    }

    @GetMapping("/departments/confirm")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String confirmation() {
        return "deptconfirm";
    }
}
