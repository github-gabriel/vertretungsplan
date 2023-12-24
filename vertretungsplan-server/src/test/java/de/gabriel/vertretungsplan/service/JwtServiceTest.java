package de.gabriel.vertretungsplan.service;

import de.gabriel.vertretungsplan.model.entities.users.Administrator;
import de.gabriel.vertretungsplan.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class JwtServiceTest {

    private final JwtService jwtService;

    public JwtServiceTest() {
        jwtService = new JwtService();
    }

    private final String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJhZG1pbmlzdHJhdG9yQGdtYWlsLmNvbSIsImlhdCI6MTUxNjIzOTAyMn0.Xx1xI6J-z9_8pD_yxn2a5eDHAGFjXi0qCUfN9Qa38Dk"; // Payload without exp for testing
    private final String email = "administrator@gmail.com";

    @Test
    @DisplayName("Retrieve Username from Token")
    public void extractUsername_shouldExtractUsernameFromToken() {
        String username = jwtService.extractUsername(token);

        assertEquals(email, username);
    }

    @Test
    @DisplayName("Retrieve a Claim from the Token")
    public void extractClaim_shouldExtractClaimFromToken() {
        String claim = jwtService.extractClaim(token, Claims::getSubject);

        assertEquals(email, claim);
    }

    @Test
    @DisplayName("Create Token from User Details")
    public void generateToken_shouldGenerateToken() {
        Administrator administrator = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );
        String generatedToken = jwtService.generateToken(new UserDetailsImpl(administrator));

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(JwtService.SECRET_KEY))
                .build()
                .parseClaimsJws(generatedToken)
                .getBody();

        assertEquals(administrator.getEmail(), claims.getSubject());
        assertFalse(claims.getExpiration().before(new Date()));
    }

    @Test
    @DisplayName("Verify Token Validity based on Username and Expiration Date")
    public void isTokenValid_shouldReturnTrueIfTokenIsValid() {
        Administrator administrator = new Administrator(
                "administrator",
                "administrator@gmail.com",
                "administrator"
        );
        UserDetailsImpl userDetails = new UserDetailsImpl(administrator);
        String generatedToken = jwtService.generateToken(userDetails);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(JwtService.SECRET_KEY))
                .build()
                .parseClaimsJws(generatedToken)
                .getBody();

        assertFalse(claims.getExpiration().before(new Date()));

        boolean isTokenValid = jwtService.isTokenValid(generatedToken, userDetails);

        assertTrue(isTokenValid);
    }

}
