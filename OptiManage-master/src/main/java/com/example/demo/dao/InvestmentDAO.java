package com.example.demo.dao;
import com.example.demo.entity.Investment;
import com.example.demo.rowmapper.InvestmentRowMapper;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class InvestmentDAO {
    private final JdbcTemplate jdbcTemplate;

    public InvestmentDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Retrieve all investments
    public List<Investment> getAllInvestments() {
        String sql = "SELECT * FROM Investment";
        return jdbcTemplate.query(sql, new InvestmentRowMapper());
    }

    // Retrieve a single investment by ID
    @SuppressWarnings("deprecation")
    public Investment getInvestmentById(int investmentId) {
        String sql = "SELECT * FROM Investment WHERE investment_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{investmentId}, new InvestmentRowMapper());
    }

    // Add a new investment
    public int addInvestment(Investment investment) {
        String sql = "INSERT INTO Investment (investor_id, amount, date_of_investment, type) VALUES (?, ?, ?, ?)";
        return jdbcTemplate.update(sql, investment.getInvestorId(), investment.getAmount(), investment.getDateOfInvestment(), investment.getType());
    }

    // Update an existing investment
    public int updateInvestment(Investment investment) {
        String sql = "UPDATE Investment SET investor_id = ?, amount = ?, date_of_investment = ?, type = ? WHERE investment_id = ?";
        return jdbcTemplate.update(sql, investment.getInvestorId(), investment.getAmount(), investment.getDateOfInvestment(), investment.getType(), investment.getInvestmentId());
    }

    // Delete an investment by ID
    public int deleteInvestment(int investmentId) {
        String sql = "DELETE FROM Investment WHERE investment_id = ?";
        return jdbcTemplate.update(sql, investmentId);
    }
}

