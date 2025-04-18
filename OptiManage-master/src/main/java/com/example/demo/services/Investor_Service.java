package com.example.demo.services;

import com.example.demo.dao.InvestorDAO;
import com.example.demo.dto.InvestorDTO;
import com.example.demo.entity.Investor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class Investor_Service {

    @Autowired
    private InvestorDAO investorDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Investor_Service(InvestorDAO investorDAO, PasswordEncoder passwordEncoder) {
        this.investorDAO = investorDAO;
        this.passwordEncoder = passwordEncoder;
    }

    public Investor registerInvestor(InvestorDTO investorDTO) {
        try {
            // Hash password
            String hashedPassword = passwordEncoder.encode(investorDTO.getPassword());
            investorDTO.setPassword(hashedPassword);

            // Convert InvestorDTO to Investor entity and save
            Investor investor = new Investor();
            investor.setInvestorId(investorDTO.getInvestorId());
            investor.setFirstName(investorDTO.getFirstName());
            investor.setMiddleName(investorDTO.getMiddleName());
            investor.setLastName(investorDTO.getLastName());
            investor.setContact(investorDTO.getContact());
            investor.setEmailAddress(investorDTO.getEmailAddress());
            investor.setRelationshipManagerId(investorDTO.getRelationshipManagerId());
            investor.setPassword(hashedPassword);
            investor.setUsername(investorDTO.getUsername());

            // Save the investor entity to the database
            investorDAO.save(investor);

            return investor;
        } catch (Exception e) {
            // Log the error and handle appropriately
            System.err.println("Error during investor registration: " + e.getMessage());
            throw e;
        }
    }

    public Investor getInvestor(String username) {
        Investor investor = investorDAO.findByUsername(username); // Assuming findByUsername is implemented in InvestorDAO
        System.out.println("Attempting to authenticate user: " + username);

        if (investor == null) {
            // If the investor is not found, throw an exception
            
            throw new UsernameNotFoundException("Investor not found with username: " + username);
        }

        // Return the found investor
        return investor;
    }
}
