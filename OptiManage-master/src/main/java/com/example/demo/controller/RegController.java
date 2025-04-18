package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.security.Principal;
import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.stereotype.Controller;

import com.example.demo.entity.Client;
import com.example.demo.entity.Employee;
import com.example.demo.entity.Investor;
import com.example.demo.services.Client_Service;
import com.example.demo.services.EmployeeService;
import com.example.demo.services.Investor_Service;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.EmployeeDTO;
import com.example.demo.dto.InvestorDTO;

@Controller
@CrossOrigin(origins = "http://localhost:8080")
public class RegController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private Client_Service clientService;

    @Autowired
    private Investor_Service investorService;

    public RegController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("employeeDTO", new EmployeeDTO());
        return "register";
    }

    private String generateRandomPassword() {
        SecureRandom random = new SecureRandom();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < 10; i++) {  // Password length of 10 characters
            password.append(characters.charAt(random.nextInt(characters.length())));
        }
        return password.toString();
    }

    
    @PostMapping("/register")
    public String register(@Valid @ModelAttribute EmployeeDTO user, BindingResult bindingResult, HttpSession session) {
        if (bindingResult.hasErrors()) {
            // Redirect or return to the registration page with error messages
            session.setAttribute("message", "Please correct the highlighted errors.");
            return "redirect:/register"; // or return "register"; if rendering the same page
        }

        if (employeeService.emailExists(user.getEmail())) {
            session.setAttribute("message", "Email already exists. Please use another email.");
            return "redirect:/register";
        }
        if (employeeService.usernameExists(user.getUsername())) {
            session.setAttribute("message", "Username already exists. Please choose a different username.");
            return "redirect:/register";
        }
        if (employeeService.contactNumberExists(user.getContactNumber())) {
            session.setAttribute("message", "Contact number already exists. Please use a different contact number.");
            return "redirect:/register";
        }
        
        Employee employee = employeeService.registerEmployee(user);
        if (employee != null) {
            session.setAttribute("message", "User registered successfully");
            return "redirect:/admin-dashboard";
        }
        session.setAttribute("message", "Registration failed! Try again.");
        return "redirect:/register";
    }

    @GetMapping("/register-client")
    public String showRegForm(Model model) {
        model.addAttribute("clientDTO", new ClientDTO());
        return "register-client";
    }

    @PostMapping("/register-client")
    public String Register(@ModelAttribute ClientDTO user, HttpSession session) {
        Client client = clientService.registerClient(user);
        if (client != null) {
            session.setAttribute("message", "User registered successfully");
            return "redirect:/login-client";
        }
        session.setAttribute("message", "Registration failed! Try again.");
        return "redirect:/register-client";
    }

    @GetMapping("/register-investor")
    public String showRegForm_i(Model model) {
        model.addAttribute("investorDTO", new InvestorDTO());
        return "register-investor";
    }

    @PostMapping("/register-investor")
    public String Register_i(@ModelAttribute InvestorDTO user, HttpSession session) {
        Investor investor = investorService.registerInvestor(user);
        if (investor != null) {
            session.setAttribute("message", "User registered successfully");
            return "redirect:/login-investor";
        }
        session.setAttribute("message", "Registration failed! Try again.");
        return "redirect:/register-investor";
    }

    @Controller
    public class InvestorController {

        @GetMapping("/investor-dashboard")
        public String showInvestorDashboard(Model model, Principal principal) {
            // Assuming you have an InvestorService to fetch the logged-in investor details
            // Investor investor = investorService.findByUsername(principal.getName());
            // model.addAttribute("investor", investor); // Add the investor object to the
            // model

            // Add investments and meetings data
            // model.addAttribute("investments", investor.getInvestments()); // Example:
            // List of investments
            // model.addAttribute("meetings", investor.getMeetings()); // Example: List of
            // meetings

            return "investor-dashboard";
        }
    }

    

}
