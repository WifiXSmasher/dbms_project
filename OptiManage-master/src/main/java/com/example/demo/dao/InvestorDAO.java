package com.example.demo.dao;
import com.example.demo.entity.Investor;
import com.example.demo.rowmapper.InvestorRowMapper;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvestorDAO {
    private final JdbcTemplate jdbcTemplate;

    public InvestorDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Investor investor) {
        String sql = "INSERT INTO Investor (first_name, middle_name, last_name, address, contact, email_address, relationship_manager_id, username, password) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, investor.getFirstName(), investor.getMiddleName(), investor.getLastName(),
                investor.getAddress(), investor.getContact(), investor.getEmailAddress(),
                investor.getRelationshipManagerId(), investor.getUsername(), investor.getPassword());
    }
    

    public Investor findByUsername(String username) {
        String sql = "SELECT * FROM Investor WHERE username = ?";
        System.out.println("Looking for investor with username: " + username);
        return jdbcTemplate.queryForObject(sql, new InvestorRowMapper(), username);
    }

    public void update(Investor investor) {
        String sql = "UPDATE Investor SET first_name = ?, middle_name = ?, last_name = ?, address = ?, contact = ?, email_address = ?, relationship_manager_id = ? WHERE investor_id = ?";
        jdbcTemplate.update(sql, investor.getFirstName(), investor.getMiddleName(), investor.getLastName(),
                investor.getAddress(), investor.getContact(), investor.getEmailAddress(), investor.getRelationshipManagerId(), investor.getInvestorId());
    }

    public Investor findById(int id) {
        String sql = "SELECT * FROM Investor WHERE investor_id = ?";
        return jdbcTemplate.queryForObject(sql, new InvestorRowMapper(), id);
    }

    public List<Investor> findAll() {
        String sql = "SELECT * FROM Investor";
        return jdbcTemplate.query(sql, new InvestorRowMapper());
    }

    public void delete(int id) {
        String sql = "DELETE FROM Investor WHERE investor_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
