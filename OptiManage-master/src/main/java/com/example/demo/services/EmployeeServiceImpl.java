package com.example.demo.services;

import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dao.SalaryDAO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeUpdateDTO;
import com.example.demo.dto.PromotionDTO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Salary;
import com.example.demo.rowmapper.EmployeeRowMapper;

import jakarta.validation.Valid;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    AuthenticationManager authManager;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private SalaryDAO salaryDAO;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public EmployeeServiceImpl(EmployeeDAO employeeDAO, PasswordEncoder passwordEncoder) {
        this.employeeDAO = employeeDAO;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Employee registerEmployee(@Valid EmployeeDTO employeeDTO) {
        try {
            String pass = employeeDTO.getPassword();
            String hashedPassword = passwordEncoder.encode(employeeDTO.getPassword());
            employeeDTO.setPassword(hashedPassword);

            Employee employee = new Employee();

            employee.setFirstName(employeeDTO.getFirstName());
            employee.setLastName(employeeDTO.getLastName());
            employee.setAddress(employeeDTO.getAddress());
            employee.setEmail(employeeDTO.getEmail());
            employee.setContactNumber(employeeDTO.getContactNumber());
            // employee.setSalary(employeeDTO.getSalary());
            employee.setDeptId(employeeDTO.getDeptId());
            employee.setDob(employeeDTO.getDob());
            employee.setUsername(employeeDTO.getUsername());
            employee.setPassword(hashedPassword);
            employee.setGender(employeeDTO.getGender());
            employee.setRole(employeeDTO.getRole());
            employee.setEmergencyContact(employeeDTO.getEmergencyContact());
            employee.setPosition(employeeDTO.getPosition());
            employee.setbankAccountNumber(employeeDTO.getBankAccountNumber());
            
            // employee.setAge(employeeDTO.getAge());

            employeeDAO.save(employee);
            emailService.sendRegistrationEmail(employeeDTO.getEmail(), employeeDTO.getUsername(), pass);

            return employee;
        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            throw e;
        }

    }

    // @Override
    // public void updateEmployee(EmployeeDTO employeeDTO) {
    //     Employee employee = new Employee();
    //     employee.setId(employeeDTO.getId());
    //     employee.setFirstName(employeeDTO.getFirstName());
    //     employee.setLastName(employeeDTO.getLastName());
    //     employee.setAddress(employeeDTO.getAddress());
    //     employee.setEmail(employeeDTO.getEmail());
    //     employee.setContactNumber(employeeDTO.getContactNumber());
    //     employee.setSalary(employeeDTO.getSalary());
    //     employee.setDeptId(employeeDTO.getDeptId());
    //     employee.setDob(employeeDTO.getDob());
    //     employee.setUsername(employeeDTO.getUsername());
    //     employee.setPassword(passwordEncoder.encode(employeeDTO.getPassword()));
    //     employee.setGender(employeeDTO.getGender());
    //     employee.setRole(employeeDTO.getRole());
    //     employee.setEmergencyContact(employeeDTO.getEmergencyContact());
    //     // employee.setAge(employeeDTO.getAge());

    //     employeeDAO.update(employee);
    // }

    @Override
    public void deleteEmployee(Long id) {
        employeeDAO.deleteById(id);
    }

    private EmployeeDTO convertToEmployeeDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setAddress(employee.getAddress());
        dto.setEmail(employee.getEmail());
        dto.setContactNumber(employee.getContactNumber());
        // dto.setSalary(employee.getSalary());
        dto.setDeptId(employee.getDeptId());
        dto.setDob(employee.getDob());
        dto.setUsername(employee.getUsername());
        dto.setGender(employee.getGender());
        dto.setRole(employee.getRole());
        dto.setEmergencyContact(employee.getEmergencyContact());
        dto.setbankAccountNumber(employee.getBankAccountNumber());
        // dto.setAge(employee.getAge());
        
        return dto;
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try {
            Employee employee = jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
            return convertToEmployeeDTO(employee);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Employee getEmployeeById2(Long id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try {
            Employee employee = jdbcTemplate.queryForObject(sql, new EmployeeRowMapper(), id);
            return employee;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void updateEmployeeProfile(Long employeeId, EmployeeUpdateDTO employeeUpdateDTO) {
    Employee employee = employeeDAO.findById(employeeId);

    if (employee == null) {
        throw new RuntimeException("Employee not found");
    }

    if (employeeUpdateDTO.getFirstName() != null) {
        employee.setFirstName(employeeUpdateDTO.getFirstName());
    }
    if (employeeUpdateDTO.getLastName() != null) {
        employee.setLastName(employeeUpdateDTO.getLastName());
    }
    if (employeeUpdateDTO.getAddress() != null) {
        employee.setAddress(employeeUpdateDTO.getAddress());
    }
    if (employeeUpdateDTO.getEmail() != null) {
        employee.setEmail(employeeUpdateDTO.getEmail());
    }
    if (employeeUpdateDTO.getContactNumber() != null) {
        employee.setContactNumber(employeeUpdateDTO.getContactNumber());
    }
    if (employeeUpdateDTO.getDob() != null) {
        employee.setDob(employeeUpdateDTO.getDob());
    }
    if (employeeUpdateDTO.getPassword() != null && !employeeUpdateDTO.getPassword().isEmpty()) {
        employee.setPassword(passwordEncoder.encode(employeeUpdateDTO.getPassword()));
    }
    if (employeeUpdateDTO.getGender() != null) {
        employee.setGender(employeeUpdateDTO.getGender());
    }
    if (employeeUpdateDTO.getEmergencyContact() != null) {
        employee.setEmergencyContact(employeeUpdateDTO.getEmergencyContact());
    }
    if (employeeUpdateDTO.getBankAccountNumber() != null) {
        employee.setbankAccountNumber(employeeUpdateDTO.getBankAccountNumber());
    }

    employeeDAO.update(employee);
}

    private Integer calculateAge(LocalDate dob) {
        return LocalDate.now().getYear() - dob.getYear();
    }

    @Override
    public Employee getLoggedInEmployee() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        String query = "SELECT * FROM employees WHERE username = ?";

        return jdbcTemplate.queryForObject(query, new EmployeeRowMapper(), username);
    }

    public Employee getEmployeeWithDepartment(Long id) {
        return employeeDAO.findByIdWithDepartment(id);
    }
    public boolean emailExists(String email) {
        return employeeDAO.existsByEmail(email); // Assume existsByEmail is implemented in employeeDAO
    }
    
    public boolean usernameExists(String username) {
        return employeeDAO.existsByUsername(username); // Assume existsByUsername is implemented in employeeDAO
    }
    
    public boolean contactNumberExists(String contactNumber) {
        return employeeDAO.existsByContactNumber(contactNumber); // Assume existsByContactNumber is implemented in employeeDAO
    }

    
    @Override
    public void promoteEmployee(Employee employee, PromotionDTO promotionDTO) {
        Long name = employee.getId();
        System.out.println("nameid=" + name);
        String currentPosition = employee.getPosition();
        String newPosition = promotionDTO.getNewPosition();
        System.out.println("newPosition" + newPosition);
        System.out.println("ncurrPosition" + currentPosition);
        double appraisalAmount = promotionDTO.getAppraisalAmount();

        // Update employee's position
        employee.setPosition(newPosition);
        employeeDAO.update(employee);

        // Update salary
        Salary salary = salaryDAO.findByEmpId(employee.getId());
        salary.setBaseSalary(salary.getBaseSalary() + appraisalAmount);
        salaryDAO.save(salary);

        // Insert into promotions table
        jdbcTemplate.update("INSERT INTO promotions (emp_id, previous_position, new_position, appraisal_amount) VALUES (?, ?, ?, ?)",
            employee.getId(), currentPosition, newPosition, appraisalAmount);
    }
}