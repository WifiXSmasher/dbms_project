package com.example.demo.controller;

import com.example.demo.dao.InvestorDAO;
import com.example.demo.entity.Investor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/investors")
public class InvestorController {

    @Autowired
    private InvestorDAO investorDAO;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String listInvestors(Model model) {
        List<Investor> investors = investorDAO.findAll();
        model.addAttribute("investors", investors);
        return "investors";  // Thymeleaf template
    }

    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAddInvestorForm(Model model) {
        model.addAttribute("investor", new Investor());
        return "addinvestor";  // Thymeleaf template
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addInvestor(@ModelAttribute Investor investor) {
        investorDAO.save(investor);
        return "redirect:/investors";  // Redirect to list after adding
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INVESTOR')")
    public String showEditInvestorForm(@PathVariable int id, Model model) {
        Investor investor = investorDAO.findById(id);
        model.addAttribute("investor", investor);
        return "editinvestor";  // Thymeleaf template
    }

    @PostMapping("/edit")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_INVESTOR')")
    public String editInvestor(@ModelAttribute Investor investor) {
        investorDAO.update(investor);
        return "redirect:/investors";  // Redirect to list after editing
    }

    @GetMapping("/confirm/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String confirmInvestor(@PathVariable int id, Model model) {
        Investor investor = investorDAO.findById(id);
        model.addAttribute("investor", investor);
        return "confirminvestor";  // Thymeleaf template
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteInvestor(@PathVariable int id) {
        investorDAO.delete(id);
        return "redirect:/investors";  // Redirect to list after deleting
    }
}
