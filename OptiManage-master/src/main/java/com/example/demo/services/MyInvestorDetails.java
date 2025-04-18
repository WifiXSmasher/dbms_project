package com.example.demo.services;

import com.example.demo.entity.Investor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MyInvestorDetails implements UserDetails {

    private Investor investor;
    private String role;

    public MyInvestorDetails(Investor investor) {
        this.investor = investor;
        this.role = "ROLE_INVESTOR";
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Role: " + role);
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return investor.getPassword();
    }

    @Override
    public String getUsername() {
        return investor.getUsername();
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

    public Investor getInvestor() {
        return investor;
    }

    public String getFirstName() {
        return investor.getFirstName();
    }

    public String getLastName() {
        return investor.getLastName();
    }

    public String getEmail() {
        return investor.getEmailAddress();
    }

    public String getContactNumber() {
        return investor.getContact();
    }
}
