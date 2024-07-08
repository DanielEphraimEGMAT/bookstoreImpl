package com.sample.bookstore1.service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    // Extracts the username from the JWT token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extracts a specific claim from the JWT token using a claims resolver function
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token); // Extracts all claims from the token
        return claimsResolver.apply(claims); // Applies the resolver function to the claims
    }

    // Generates a JWT token with default claims for the given user details
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails); // Calls generateToken with an empty map for extra claims
    }

    // Generates a JWT token with extra claims for the given user details
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration); // Calls buildToken with extra claims, user details,
                                                                    // and expiration time
    }

    // Returns the JWT expiration time
    public long getExpirationTime() {
        return jwtExpiration;
    }

    // Builds a JWT token with the given claims, user details, and expiration time
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration) {
        return Jwts
                .builder() // Creates a new JWT builder
                .setClaims(extraClaims) // Sets the extra claims
                .setSubject(userDetails.getUsername()) // Sets the subject (username)
                .setIssuedAt(new Date(System.currentTimeMillis())) // Sets the issued at time to the current time
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Sets the expiration time
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Signs the token with the secret key using HS256
                                                                    // algorithm
                .compact(); // Builds the token and returns it as a compact string
    }

    // Validates the JWT token for the given user details
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token); // Extracts the username from the token
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token); // Checks if the username matches
                                                                                       // and the token is not expired
    }

    // Checks if the JWT token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date()); // Checks if the token's expiration date is before the
                                                            // current date
    }

    // Extracts the expiration date from the JWT token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration); // Calls extractClaim with the token and a function to get
                                                           // the expiration date
    }

    // Extracts all claims from the JWT token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder() // Creates a new JWT parser builder
                .setSigningKey(getSignInKey()) // Sets the signing key
                .build() // Builds the parser
                .parseClaimsJws(token) // Parses the JWT token
                .getBody(); // Returns the body of the token, which contains the claims
    }

    // Retrieves the signing key from the secret key
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Decodes the secret key from Base64
        return Keys.hmacShaKeyFor(keyBytes); // Returns the HMAC-SHA key
    }
}