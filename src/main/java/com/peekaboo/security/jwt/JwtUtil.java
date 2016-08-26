package com.peekaboo.security.jwt;


import com.peekaboo.controller.sign.SignResponse;
import com.peekaboo.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Class is used for encapsulating token generation and parsing.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;


    /**
     * Tries to parse specified String as a JWT token. If successful, returns User object with username, id and role prefilled (extracted from token).
     * If unsuccessful (token is invalid or not containing all required user properties), simply returns null.
     *
     * @param token the JWT token to parse
     * @return the User object extracted from specified token or null if a token is invalid.
     */
    public User parse(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

            User u = new User();
            u.setLogin(body.getSubject());
            u.setId((Long) body.get("userId"));
            u.setRoles((int) body.get("role"));
            u.setEnabled((boolean) body.get("enabled"));

            return u;
        } catch (JwtException | ClassCastException | IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Generates a JWT token containing username as subject, and userId and role as additional claims. These properties are taken from the specified
     * User object. Tokens validity is infinite.
     *
     * @param signResponse the user for which the token will be generated
     * @return the JWT token
     */
    public String generateToken(SignResponse signResponse) {
        Claims claims = Jwts.claims().setSubject(signResponse.getUsername());
        claims.put("userId", signResponse.getId());
        claims.put("role", signResponse.getRole());
        claims.put("enabled", signResponse.getEnabled());

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
