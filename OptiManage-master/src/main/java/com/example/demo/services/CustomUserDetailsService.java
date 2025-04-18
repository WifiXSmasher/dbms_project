package com.example.demo.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dao.EmployeeDAO;
import com.example.demo.entity.Employee;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private EmployeeDAO employeeDAO;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Employee employee = employeeDAO.findByUsername(username);
        // Client client = clientDAO.findByUsername(username);
        // Investor investor = investorDAO.findByUsername(username);

        if (employee != null) {
            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(employee.getRole()));
            return new MyUserDetails(employee);
        }
        // else if (client != null) {
        //     List<GrantedAuthority> authorities = new ArrayList<>();
        //     authorities.add(new SimpleGrantedAuthority("ROLE_CLIENT")); // Assigning a specific role for clients
        //     return new MyClientDetails(client);
        // }
        // else if (investor != null) {
        //     List<GrantedAuthority> authorities = new ArrayList<>();
        //     authorities.add(new SimpleGrantedAuthority("ROLE_INVESTOR")); // Assigning a specific role for investors
        //     return new MyInvestorDetails(investor);
        // }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }

    public boolean existsByEmail(String email) {
        return employeeDAO.existsByEmail(email);
    }

    // CustomUserDetailsService.java
    public boolean updatePassword(String email, String newPassword) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(); // create only for this method
    
        Employee employee = employeeDAO.findByEmail(email);
        if (employee == null) {
            return false; // Email not found
        }
        
        // Print the `reports_to` value to the terminal
        System.out.println("reports_to for employee with email " + email + ": " + employee.getreportsTo());
    
        // Encode the new password and update the employee record
        employee.setPassword(encoder.encode(newPassword));
        employeeDAO.update(employee);
        return true;
    }
    

}
