package com.example.demo.rowmapper;

import org.springframework.jdbc.core.RowMapper;
import com.example.demo.entity.Investment;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvestmentRowMapper implements RowMapper<Investment> {
    @Override
    public Investment mapRow(ResultSet rs, int rowNum) throws SQLException {
        Investment investment = new Investment();
        investment.setInvestmentId(rs.getInt("investment_id"));
        investment.setInvestorId(rs.getInt("investor_id"));
        investment.setAmount(rs.getDouble("amount"));
        investment.setDateOfInvestment(rs.getDate("date_of_investment"));
        investment.setType(rs.getString("type"));
        return investment;
    }
}
