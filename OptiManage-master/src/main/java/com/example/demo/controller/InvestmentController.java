package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import com.example.demo.entity.Investment;
import com.example.demo.dao.InvestmentDAO;

@Controller
@RequestMapping("/investments")
public class InvestmentController {
    @Autowired
    private InvestmentDAO investmentDAO;

    // Display all investments
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAllInvestments(Model model) {
        List<Investment> investments = investmentDAO.getAllInvestments();
        model.addAttribute("investments", investments);
        return "investments"; // Thymeleaf template name
    }

    // Show form to add a new investment
    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddInvestmentForm(Model model) {
        model.addAttribute("investment", new Investment());
        return "addinvestment"; // Thymeleaf template name
    }

    // Handle form submission to add a new investment
    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addInvestment(@ModelAttribute Investment investment) {
        investmentDAO.addInvestment(investment);
        return "redirect:/investments";
    }

    // Show form to edit an existing investment
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showEditInvestmentForm(@PathVariable int id, Model model) {
        Investment investment = investmentDAO.getInvestmentById(id);
        model.addAttribute("investment", investment);
        return "editInvestment"; // Thymeleaf template name
    }

    // Handle form submission to update an investment
    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String editInvestment(@ModelAttribute Investment investment) {
        investmentDAO.updateInvestment(investment);
        return "redirect:/investments";
    }

    // Delete an investment
    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteInvestment(@PathVariable int id) {
        investmentDAO.deleteInvestment(id);
        return "redirect:/investments";
    }
}

