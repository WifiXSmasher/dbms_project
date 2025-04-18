package com.example.demo.controller;

import java.net.URI;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dao.ClientDAO;
import com.example.demo.dao.EmployeeDAO;
import com.example.demo.dto.AuthRequestDTO;
import com.example.demo.entity.Client;
import com.example.demo.entity.Investor;
import com.example.demo.security.jwt.JwtUtil;
import com.example.demo.services.CustomUserDetailsService;
import com.example.demo.services.EmailService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private EmployeeDAO employeeDAO;

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private EmailService emailService;

    @GetMapping("/emp-login")
    public ResponseEntity<?> redirectToLogin() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/login.html"))
                .build();
    }

    @PostMapping("/emp-login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO customer) {
        System.out.println("##Authenticating user: ");
        String username = customer.getUsername();
        String password = customer.getPassword();
        System.out.println("##Authenticating user: " + username + password);

        try {
            // Authenticate user using username and password
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            System.out.println("##Authenticated user: " + username);

            // Set the authenticated user in SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Collect roles from authentication
            List<String> roles = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.toList());
            System.out.println("Generated roles: " + roles);

            // Generate JWT token with assigned roles
            String token = jwtUtil.generateToken(authentication, roles);
            System.out.println("Generated token: " + token);

            // Redirect based on role
            String redirectUrl;
            if (roles.contains("ROLE_ADMIN")) {
                redirectUrl = "/admin-dashboard";
            } else if (roles.contains("ROLE_HOD")) {
                redirectUrl = "/hod-dashboard";
            }
            else{
                redirectUrl = "/emp-dashboard";   
            }

            // Prepare and return response with token and redirect URL
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("token", token);
            jsonResponse.put("redirectUrl", redirectUrl);
            return ResponseEntity.ok(jsonResponse);
        } catch (AuthenticationException e) {
            System.out.println("Error: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed. Please check your credentials and try again.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred during login. Please try again later.");
        }
    }
    
    
    

    @GetMapping("/forgot-password")
    public ResponseEntity<?> forgotPasswordPage() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/forgot-password.html"))
                .build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        // 1. Check if the email exists in the database
        if (userDetailsService.existsByEmail(email)) {
            // 2. Generate a token for password reset
            String token = jwtUtil.generatePasswordResetToken(email);

            // 3. Send email with the token or reset link
            String resetLink = "http://localhost:8080/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);

            return ResponseEntity.ok("Password reset link has been sent to your email.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email not registered.");
        }
    }

    @GetMapping("/reset-password")
    public ResponseEntity<?> resetPasswordPage(@RequestParam String token) {
        // You can validate the token here if needed
        if (!jwtUtil.validatePasswordResetToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }

        // If valid, redirect to the reset password HTML page
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/reset-password.html?token=" + token))
                .build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        System.out.println("Received reset password request: " + request);
        String token = request.get("token");
        String newPassword = request.get("password");

        // Validate the token
        if (!jwtUtil.validatePasswordResetToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired token.");
        }

        String email;
        try {
            email = jwtUtil.getEmailFromToken(token);
            System.out.println("Extracted email from token: " + email);
        } catch (Exception e) {
            System.out.println("Error extracting email from token: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid token.");
        }
        // Update the password in the database
        System.out.println("Attempting to update password for email: " + email);

        if (userDetailsService.updatePassword(email, newPassword)) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to reset password.");
        }
    }

    @GetMapping("/login-client")
    public ResponseEntity<?> redirectToLogin_c() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/login-client.html"))
                .build();
    }

    @PostMapping("/login-client")
    public ResponseEntity<?> clientLogin(@RequestBody Client client) {
        System.out.println("##Authenticating client: ");
        String username = client.getUsername();
        String password = client.getPassword();

        try {
            System.out.println("##Authenticating client: " + username + password);

            // Fetch client directly from ClientDAO
            Client authenticatedClient = clientDAO.findByUsername(username);
            if (authenticatedClient == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Client not found");
            }

            // Manually authenticate if needed
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT Token
            String token = jwtUtil.generateToken(authentication, Arrays.asList("ROLE_CLIENT"));

            // Return the token and redirect URL
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("token", token);
            jsonResponse.put("redirectUrl", "/clients/dashboard/" + authenticatedClient.getClientId());
            return ResponseEntity.ok(jsonResponse);
        } catch (AuthenticationException e) {
            System.out.println("##ERROR: " + e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect credentials");
        }
    }

    @GetMapping("/login-investor")
    public ResponseEntity<?> redirectToLogin_i() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("/login-investor.html"))
                .build();
    }

    @PostMapping("/login-investor")
    public ResponseEntity<?> investorLogin(@RequestBody Investor investor) {
        System.out.println("##Authenticating user: ");
        String username = investor.getUsername();
        String password = investor.getPassword();
        System.out.println("##Authenticating user: " + username + password);
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            (username), password));

            // Set the authentication in the security context
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate JWT Token
            String token = jwtUtil.generateToken(authentication, Arrays.asList("ROLE_INVESTOR"));

            // Return the token and redirect URL
            Map<String, String> jsonResponse = new HashMap<>();
            jsonResponse.put("token", token);
            jsonResponse.put("redirectUrl", "/investor-dashboard");
            System.out.println(token);
            return ResponseEntity.ok(jsonResponse);
        } catch (AuthenticationException e) {
            System.out.println("ERROR" + e);
            System.out.println("##Authenticating user: " + username + password);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect credentials");
        }
    }

    @GetMapping("/logout")
    public String Logout(HttpSession session) {
        session.invalidate(); // Invalidate session
        // window.localstorage.removeItem("token");
        return "redirect:/emp-login"; // Redirect to login page
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        // Invalidate session and clear context
        SecurityContextHolder.clearContext();

        // Set an expired cookie to remove the JWT token on the client
        Cookie jwtCookie = new Cookie("token", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true); // Ensure this is true in production (for HTTPS)
        jwtCookie.setPath("/emp-login");
        jwtCookie.setMaxAge(0); // Immediately expire the cookie

        // Add cookie to the response
        response.addCookie(jwtCookie);

        return (ResponseEntity<?>) ResponseEntity.ok();
    }

}
