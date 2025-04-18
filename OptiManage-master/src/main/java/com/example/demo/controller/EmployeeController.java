package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;  // Import ClientDAO from the dao package
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.dao.DepartmentDAO;  // Import ClientDAO from the dao package
import com.example.demo.dao.EmployeeDAO;  // Import Client from the entity package
import com.example.demo.dao.NotificationDAO;
import com.example.demo.dao.SalaryDAO;  // Import Client from the entity package
import com.example.demo.dto.EmployeeUpdateDTO;
import com.example.demo.dto.PromotionDTO;
import com.example.demo.entity.Department;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Salary;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.SalaryService;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpSession;

@Controller
public class EmployeeController {

    @Inject
    private EmployeeDAO employeeDAO;

    @Autowired
    private DepartmentDAO departmentDAO;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private SalaryDAO salaryDAO;
    @Autowired
    private NotificationDAO notificationDAO;

    @Autowired
    private SalaryService salaryService;

    // Show all employees excluding HR
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/employee/list")

    public String showEmployeeList(Model model) {
        Map<String, List<Employee>> employeesByDepartment = employeeDAO.findAllByDepartmentExceptHR();
        model.addAttribute("employeesByDepartment", employeesByDepartment);
        return "employee_list";  // Make sure this matches your HTML filename
    }



    // Show form to input working/absent days for an employee
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/hr/generate-salary-slip/{id}")
    public String showGenerateSalarySlipForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeDAO.findById(id);
        model.addAttribute("employee", employee);
        return "generate-salary-slip"; // A Thymeleaf template to input working/absent days
    }

    // Generate salary slip based on inputted working and absent days
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @PostMapping("/hr/generate-salary-slip")
    public String generateSalarySlip(
            @RequestParam("empId") Long empId,
            @RequestParam("workingDays") int workingDays,
            @RequestParam("absentDays") int absentDays,
            Model model) {
        Employee employee = employeeDAO.findById(empId);
        Salary salary = salaryService.calculateSalarySlip(employee.getId(), workingDays, absentDays);
        salaryDAO.save(salary);

        model.addAttribute("salary", salary);
        return "salary-slip"; // A Thymeleaf template to display the generated salary slip
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin-dashboard")
    public String home() {
        return "home"; // Points to your new home.html file
    }

    // Display Employee Dashboard
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    @GetMapping("/emp-dashboard")
    public String showEmployeeDashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long id = employee.getId();
        boolean hasReportees = !employeeDAO.getEmployeesByManagerId(id).isEmpty();
        
        model.addAttribute("hasReportees", hasReportees);
        model.addAttribute("employeeId", id);
        model.addAttribute("employee", employee);
        
        // Pass the department ID to the model
        model.addAttribute("departmentId", employee.getDeptId());
        
        return "emp-dashboard";  // Points to emp-dashboard.html
    }

    
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/employees")  // Changed this line
    public String employees(Model m) {  // Renamed the method to employees
        // Fetch all employees using JDBC
        m.addAttribute("emp", employeeDAO.findAll());
        return "employees";  // Points to your employees.html file
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/addemp")
    public String addEmp(Model model, HttpSession session) {
        String message = (String) session.getAttribute("message");
        if (message != null) {
            model.addAttribute("message", message);
            session.removeAttribute("message");
        }
        // Fetch all employees to display in the dropdown
        List<Department> departments = departmentDAO.findAll();
        model.addAttribute("departments", departments);
        return "register";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employees")
    public String submitForm(Employee employee, HttpSession session) {
        employeeDAO.save(employee); // Save employee using JDBC
        session.setAttribute("message", "Employee has been added successfully!");
        return "redirect:/employees";
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/employees/confirm")
    public String confirmForm(Model model) {
        return "employeeConfirm"; // Ensure this is the right template name
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/employees/confirm")
    public String confirmForm(Employee employee, Model model) {
        model.addAttribute("employee", employee);
        return "employeeConfirm";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model m) {
        Employee e = employeeDAO.findById(id);
        if (e != null) {
            m.addAttribute("emp", e);
            // Fetch all employees to display in the dropdown
        List<Department> departments = departmentDAO.findAll();
        m.addAttribute("departments", departments);
            return "edit";
        } else {
            return "redirect:/addemp";
        }
        
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable Long id, HttpSession session) {
        if (employeeDAO.existsById(id)) {
            employeeDAO.deleteById(id); // Delete employee using JDBC
            session.setAttribute("message", "Employee has been deleted successfully!");
        } else {
            session.setAttribute("message", "Employee not found!");
        }
        return "redirect:/employees";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/employees/search")
    public String searchEmployees(@RequestParam("keyword") String keyword, Model model) {
        List<Employee> employees = employeeDAO.searchByName(keyword);
        model.addAttribute("emp", employees);  // "emp" is the attribute for the employee list
        return "employees";  // Return to the same employees page with search results
    }

    // Display the form to update the profile
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/employees/update")
    public String showUpdateForm(Model model) {
        // EmployeeDTO employee = employeeService.getEmployeeById(id);
        // Get the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        EmployeeUpdateDTO employeeUpdateDTO = new EmployeeUpdateDTO();
        employeeUpdateDTO.setFirstName(employee.getFirstName());
        employeeUpdateDTO.setLastName(employee.getLastName());
        employeeUpdateDTO.setAddress(employee.getAddress());
        employeeUpdateDTO.setEmail(employee.getEmail());
        employeeUpdateDTO.setContactNumber(employee.getContactNumber());
        employeeUpdateDTO.setGender(employee.getGender());
        employeeUpdateDTO.setEmergencyContact(employee.getEmergencyContact());
        employeeUpdateDTO.setDob(employee.getDob());
        model.addAttribute("employeeUpdateDTO", employeeUpdateDTO);
        return "updateProfile";  // This corresponds to a Thymeleaf template
    }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @PostMapping("/employees/update")
    public String updateEmployee(@ModelAttribute("employeeUpdateDTO") EmployeeUpdateDTO employeeUpdateDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "updateProfile";
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Employee employee = employeeDAO.findByUsername(username);
        Long id = employee.getId();
        // Employee employee = employeeService.getEmployeeById(id);
        employeeService.updateEmployeeProfile(id, employeeUpdateDTO);

        model.addAttribute("successMessage", "Profile updated successfully");
        return "redirect:/employees/update";
    }

    // Handle form submission to update the employee profile
    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    // @PostMapping("/employees/update")
    // public String updateEmployeeProfile(@ModelAttribute("employeeUpdateDTO") EmployeeUpdateDTO employeeUpdateDTO, RedirectAttributes redirectAttributes) {
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //     String username = authentication.getName();

    //     // Fetch the employee details by username
    //     Employee employee = employeeDAO.findByUsername(username);
    //     Long id = employee.getId();
    //     employeeService.updateEmployeeProfile(id, employeeUpdateDTO);
    //     // Add a success message as a flash attribute
    //     redirectAttributes.addFlashAttribute("successMessage", "Profile updated successfully!");
    //     return "redirect:/employees/dashboard/" + id;
    // }

    // @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    // @PostMapping("/employees/update")
    // public String updateEmp(@ModelAttribute Employee e, HttpSession session) {
    //     employeeDAO.update(e); // Update employee using JDBC
    //     session.setAttribute("msg", "Employee data updated successfully!");
    //     return "redirect:/employees";
    // }
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/profile")
    public String showEmployeeProfile(Model model) {
        // Get the authenticated user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);

        // Add employee details to the model
        model.addAttribute("employee", employee);
        
        return "employee-profile";  // Thymeleaf template name
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_EMPLOYEE', 'ROLE_HOD')")
    @GetMapping("/notifications")
    public String getNotifications( Model model) {
        // Fetch notifications for the given employee ID
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long id = employee.getId();
        List<Notification> notifications = notificationDAO.getNotificationsByEmpId(id);

        // Add notifications to the model to be used in the view
        model.addAttribute("notifications", notifications);
        
        return "notif/notif"; // Return the name of the notifications view
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HOD')")
    @GetMapping("/employees/promote/{id}")
    public String showPromotionForm(@PathVariable("id") Long id, Model model) {
        Employee employee = employeeService.getEmployeeById2(id);
        PromotionDTO promotionDTO = new PromotionDTO();
        promotionDTO.setId(employee.getId());
        
        promotionDTO.setCurrentPosition(employee.getPosition());
        System.out.println("Current Position of Employee ID " + id + ": " + employee.getPosition());
        model.addAttribute("promotionDTO", promotionDTO);
        return "promotion";  // This corresponds to the Thymeleaf template
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_HOD')")
    @PostMapping("/employees/promote/{id}")
    public String promoteEmployee(@PathVariable("id") Long id, @ModelAttribute PromotionDTO promotionDTO, Model model) {
        // Fetch the employee details by ID
        Employee employee = employeeService.getEmployeeById2(id);

        // Perform the promotion
        employeeService.promoteEmployee(employee, promotionDTO);

        // Redirect or return to a confirmation page
        return "redirect:/hod-dashboard";  // Adjust this redirect as necessary
    }
    @PreAuthorize("hasRole('ROLE_HOD')")
    @GetMapping("/hod-dashboard")
public String showHodDashboard(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long id = employee.getId();

    Employee hod = employeeDAO.findById(id);
    if (!"ROLE_HOD".equals(hod.getRole())) {
        return "error/403";  // Redirect to an error page if the employee is not an HOD
    }
    boolean hasReportees = !employeeDAO.getEmployeesByManagerId(id).isEmpty();
    model.addAttribute("hasReportees", hasReportees);
    model.addAttribute("employeeId", id);
    model.addAttribute("employee", hod);
    return "hod-dashboard";  // Render hod-dashboard.html
}

@PreAuthorize("hasRole('ROLE_HOD')")
@GetMapping("/department/employees")
public String showDepartmentEmployees(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // Fetch the employee details by username
        Employee employee = employeeDAO.findByUsername(username);
        Long id = employee.getId();

    List<Employee> departmentEmployees = employeeDAO.findEmployeesByHod(id);
    model.addAttribute("employees", departmentEmployees);
    return "department-employees";
}

}





