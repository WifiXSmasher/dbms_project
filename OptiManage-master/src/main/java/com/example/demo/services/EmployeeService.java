package com.example.demo.services;

// import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.EmployeeUpdateDTO;
import com.example.demo.dto.PromotionDTO;
import com.example.demo.entity.Employee;

public interface EmployeeService {
    Employee registerEmployee(EmployeeDTO employeeDTO);
    EmployeeDTO getEmployeeById(Long id);
    // void updateEmployee(EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    void updateEmployeeProfile(Long employeeId, EmployeeUpdateDTO employeeUpdateDTO);//used by employee
    Employee getLoggedInEmployee();
    Employee getEmployeeWithDepartment(Long id);
    boolean emailExists(String email);
    boolean usernameExists(String username);
    boolean contactNumberExists(String contactNumber);
    
    Employee getEmployeeById2(Long id);
    void promoteEmployee(Employee employee, PromotionDTO promotionDTO);
}

