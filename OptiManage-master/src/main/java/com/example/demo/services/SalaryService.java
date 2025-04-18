package com.example.demo.services;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Salary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.SalaryDAO;
import java.math.BigDecimal;

@Service
public class SalaryService {

    private final SalaryDAO salaryDAO;

    @Autowired
    public SalaryService(SalaryDAO salaryDAO) {
        this.salaryDAO = salaryDAO;
    }

    public Salary calculateSalarySlip(Long empId, int workingDays, int absentDays) {
        // Retrieve existing salary details
        Salary salary = salaryDAO.findByEmpId(empId);

        double baseSalary = salary.getBaseSalary();
        int totalDays = workingDays + absentDays;
        double dailySalary = baseSalary / totalDays;

        // Get the number of leave days taken in the current month
        int leaveDaysTaken = salaryDAO.getLeaveDaysForCurrentMonth(empId);

        // Calculate salary after excluding the leave days from the absent days
        int effectiveAbsentDays = absentDays - leaveDaysTaken;

        if (effectiveAbsentDays < 0) {
            effectiveAbsentDays = 0; // Prevent negative absent days
        }

        // Calculate the salary for the working days
        double calculatedSalary = dailySalary * workingDays;

        // Adjust salary based on allowances and deductions
        double hra = (salary.getHra() / 100) * calculatedSalary;
        double medicalAllowance = salary.getMedicalAllowance();
        double travelAllowance = salary.getTravelAllowance();
        double pfDeduction = (salary.getPfDeduction() / 100) * calculatedSalary;

        // Deduct salary based on the remaining absent days
        double absentDeduction = dailySalary * effectiveAbsentDays;

        // Update salary fields
        salary.setBaseSalary(calculatedSalary - absentDeduction); // Deduct salary for absent days
        salary.setHra(hra);
        salary.setMedicalAllowance(medicalAllowance);
        salary.setTravelAllowance(travelAllowance);
        salary.setPfDeduction(pfDeduction);

        return salary;
    }
}
