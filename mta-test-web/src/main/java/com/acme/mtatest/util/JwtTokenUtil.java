package com.acme.mtatest.util;

import java.util.Date;

import javax.enterprise.context.ApplicationScoped;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@ApplicationScoped
public class JwtTokenUtil {

    private static final Logger logger = LogManager.getLogger(JwtTokenUtil.class);

    // In production, retrieved from JBoss vault via jboss-vault-library
    private static final String SECRET_KEY = "acme-mtatest-secret-key";
    private static final long EXPIRATION_MS = 3600000; // 1 hour

    public String generateToken(String username, String accountNumber) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .setSubject(username)
                .claim("accountNumber", accountNumber)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Claims claims = extractClaims(token);
            return claims.getExpiration().after(new Date());
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expired");
        } catch (MalformedJwtException e) {
            logger.warn("Malformed JWT token");
        } catch (SignatureException e) {
            logger.warn("Invalid JWT signature");
        } catch (Exception e) {
            logger.warn("JWT validation error: {}", e.getMessage());
        }
        return false;
    }

    public String getUsernameFromToken(String token) {
        return extractClaims(token).getSubject();
    }

    public String getAccountNumberFromToken(String token) {
        return extractClaims(token).get("accountNumber", String.class);
    }

    private Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
