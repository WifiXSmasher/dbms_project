package com.example.demo.rowmapper;

import com.example.demo.entity.Investor;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class InvestorRowMapper implements RowMapper<Investor> {
    @Override
    public Investor mapRow(ResultSet rs, int rowNum) throws SQLException {
        Investor investor = new Investor();
        investor.setInvestorId(rs.getInt("investor_id"));
        investor.setFirstName(rs.getString("first_name"));
        investor.setMiddleName(rs.getString("middle_name"));
        investor.setLastName(rs.getString("last_name"));
        investor.setAddress(rs.getString("address"));
        investor.setContact(rs.getString("contact"));
        investor.setEmailAddress(rs.getString("email_address"));
        investor.setRelationshipManagerId(rs.getLong("relationship_manager_id"));
        investor.setUsername(rs.getString("username"));
        investor.setPassword(rs.getString("password"));
        return investor;
    }
}
