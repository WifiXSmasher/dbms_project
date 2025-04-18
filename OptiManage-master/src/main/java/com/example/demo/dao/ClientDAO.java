package com.example.demo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import com.example.demo.entity.Client;
import com.example.demo.rowmapper.ClientRowMapper;

import java.util.List;

@Repository
public class ClientDAO {
    private final JdbcTemplate jdbcTemplate;

    public ClientDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Client> getAllClients() {
        String sql = "SELECT * FROM Client";
        return jdbcTemplate.query(sql, new ClientRowMapper());
    }

    public Client findByUsername(String username) {
        String sql = "SELECT * FROM Client WHERE username = ?";
        return jdbcTemplate.queryForObject(sql, new ClientRowMapper(), username);
    }

    @SuppressWarnings("deprecation")
    public Client getClientById(int clientId) {
        String sql = "SELECT * FROM Client WHERE client_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{clientId}, new ClientRowMapper());
    }

    public int addClient(Client client) {
        String sql = "INSERT INTO Client (first_name, middle_name, last_name, contact_number, email_address, client_type) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, client.getFirstName(), client.getMiddleName(), client.getLastName(), client.getContactNumber(), client.getEmailAddress(), client.getClientType());
    }

    public int updateClient(Client client) {
        String sql = "UPDATE Client SET first_name = ?, middle_name = ?, last_name = ?, contact_number = ?, email_address = ?, client_type = ? WHERE client_id = ?";
        return jdbcTemplate.update(sql, client.getFirstName(), client.getMiddleName(), client.getLastName(), client.getContactNumber(), client.getEmailAddress(), client.getClientType(), client.getClientId());
    }

    public int deleteClient(int clientId) {
        String sql = "DELETE FROM Client WHERE client_id = ?";
        return jdbcTemplate.update(sql, clientId);
    }

    public void save(Client client) {
        String sql = "INSERT INTO Client (first_name, middle_name, last_name, contact_number, email_address, client_type, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            client.getFirstName(),         // First Name
            client.getMiddleName(),        // Middle Name
            client.getLastName(),          // Last Name
            client.getContactNumber(),     // Contact Number
            client.getEmailAddress(),      // Email Address
            client.getClientType(),        // Client Type
            client.getUsername(),          // Username
            client.getPassword()           // Password (hashed)
        );
    }
    
}

