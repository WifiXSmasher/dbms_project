package com.example.demo.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    private String secretkey = "mySuperSecretKeyThatIsVeryLongAndSecure!"; // Replace with a secure key

   

    @SuppressWarnings("deprecation")
    public String generateToken(String username) {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30))  // 30 hours
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    @SuppressWarnings("deprecation")
    public String generateToken(Authentication authentication, List<String> roles) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 30))  // 30 hours
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    @SuppressWarnings("deprecation")
    public String createToken(Map<String, Object> claims, String subject, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, getKey())
                .compact();
    }

    // Method to generate a password reset token specifically
    public String generatePasswordResetToken(String email) {
        Map<String, Object> claims = Map.of("email", email);
        return createToken(claims, email, 1000*60*15);
    }

    public boolean validatePasswordResetToken(String token) {
        // Check if the token is expired
        if (isTokenExpired(token)) {
            return false;
        }
    
        // Optionally, extract the email from the token to validate or check against your database
        String email = extractClaim(token, claims -> claims.get("email", String.class));
    
        // Here, you could add additional checks, such as checking if the email exists in the database
        // For now, we just return true if the token is valid and not expired
        return email != null && !email.isEmpty();
    }
    
    public String getEmailFromToken(String token) {
        return extractClaim(token, claims -> claims.get("email", String.class));
    }

    @SuppressWarnings("unchecked")
    public List<String> getRolesFromToken(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }
    

    private SecretKey getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(Base64.getEncoder().encodeToString(secretkey.getBytes())); // Ensure key is Base64 encoded
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateToken(String token, String username) {
        final String userName = extractUserName(token);
        return (userName.equals(username) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
    
}
