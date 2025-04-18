// package com.example.demo.services;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.example.demo.dao.EmployeeDAO;
// import com.example.demo.dao.PromotionDAO;
// import com.example.demo.dao.SalaryDAO;
// import com.example.demo.dto.PromotionDTO;
// import com.example.demo.entity.Employee;
// import com.example.demo.entity.Promotion;
// import com.example.demo.entity.Salary;

// @Service
// public class PromotionService {

//     private final PromotionDAO promotionDAO;
//     private final EmployeeDAO employeeDAO;
//     private final SalaryDAO salaryDAO;

//     @Autowired
//     public PromotionService(PromotionDAO promotionDAO, EmployeeDAO employeeDAO, SalaryDAO salaryDAO) {
//         this.promotionDAO = promotionDAO;
//         this.employeeDAO = employeeDAO;
//         this.salaryDAO = salaryDAO;
//     }

    
//     @Transactional
//     public void promoteEmployee(PromotionDTO promotionDTO) {
//         Employee employee = employeeDAO.findById(promotionDTO.getId());

//         if (employee != null) {
//             String previousPosition = employee.getPosition();
//             String currentPosition = promotionDTO.getNewPosition();
//             double appraisalAmount = promotionDTO.getAppraisalAmount();

//             // Update employee's position
//             employee.setPosition(currentPosition);
//             employeeDAO.update(employee);

//             // Update salary
//             Salary salary = salaryDAO.findByEmpId(employee.getId());
//             double newAmount = salary.getBaseSalary() + appraisalAmount;
//             salary.setBaseSalary(newAmount);
//             salaryDAO.save(salary);

//             // Record the promotion
//             Promotion promotion = new Promotion();
//             promotion.setEmpId(employee.getId());
//             promotion.setPreviousPosition(previousPosition);
//             promotion.setNewPosition(currentPosition);
//             promotion.setAppraisalAmount(appraisalAmount);

//             promotionDAO.savePromotion(promotion,employee);
//         }
//     }

//     public List<Promotion> getPromotionsByEmployeeId(Long employeeId) {
//         return promotionDAO.getPromotionsByEmployeeId(employeeId);
//     }
// }
