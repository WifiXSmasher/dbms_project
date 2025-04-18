package com.example.demo.services;

import com.example.demo.entity.Client;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MyClientDetails implements UserDetails {

    private Client client;
    private String role;

    public MyClientDetails(Client client) {
        this.client = client;
        this.role = "ROLE_CLIENT";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Role: " + role);
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return client.getPassword();
    }

    @Override
    public String getUsername() {
        return client.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Client getClient() {
        return client;
    }

    public String getFirstName() {
        return client.getFirstName();
    }

    public String getLastName() {
        return client.getLastName();
    }

    public String getEmail() {
        return client.getEmailAddress();
       }

    public String getContactNumber() {
        return client.getContactNumber();
    }
}
