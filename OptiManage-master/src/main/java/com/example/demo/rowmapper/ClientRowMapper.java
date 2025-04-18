package com.example.demo.rowmapper;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import com.example.demo.entity.Client;


public class ClientRowMapper implements RowMapper<Client> {

    @Override
    public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
        Client client = new Client();
        client.setClientId(rs.getInt("client_id"));
        client.setFirstName(rs.getString("first_name"));
        client.setMiddleName(rs.getString("middle_name"));
        client.setLastName(rs.getString("last_name"));
        client.setContactNumber(rs.getString("contact_number"));
        client.setEmailAddress(rs.getString("email_address"));
        client.setClientType(rs.getString("client_type"));
        client.setUsername(rs.getString("username"));
        client.setPassword(rs.getString("password"));
        return client;
    }
}

