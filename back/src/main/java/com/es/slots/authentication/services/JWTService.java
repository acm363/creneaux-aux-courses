package com.es.slots.authentication.services;

import com.es.slots.user.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JWTService {

    private final AuthenticationUserService authenticationUserService;
    /**
     * Generates a JWT for a given {@link UserDetails}.
     * Incorporates user-specific claims such as name, email, publicId, and type.
     *
     * @param userDetails The {@link UserDetails} for which the JWT is generated.
     * @return The generated JWT.
     */
    public String generateToken(UserDetails userDetails) {
        User loggedUsed = authenticationUserService.loadUserByEmail(userDetails.getUsername());
        int dayDuration = 86400000;
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("name", loggedUsed.getName())
                .claim("email", loggedUsed.getEmail())
                .claim("publicId", loggedUsed.getPublicId())
                .claim("type", loggedUsed.getType())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + dayDuration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * Extracts the username from a given JWT.
     * @param token
     * @return The username.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    /**
     * Extracts a specific claim from a given JWT.
     * @param token
     * @param claimsResolver
     * @param <T>
     * @return The claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    /**
     * Generates a {@link Key} for signing JWTs.
     * @return The generated {@link Key}.
     */
    private Key getSignInKey() {
        byte[] secret = Decoders.BASE64.decode("73b0c7ff5362ed260e890135c3bb902890b0a4523112b104523c05e41474785b1a6698f82379be287a9579afe6fe6b2a0801d82fd41546d0b515d1df91bb0446afb84a23a9df3ae09f616f89cdc128563ae7e4505d005863b3530ba480ac58a02d399");
        return Keys.hmacShaKeyFor(secret);
    }
    /**
     * Extracts all claims from a given JWT.
     * @param token
     * @return  A {@link Claims} object containing all claims.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
    }

    /**
     * Validates whether a given JWT is valid for a specific {@link UserDetails}.
     *
     * @param token The JWT to validate.
     * @param userDetails The {@link UserDetails} against which to validate the JWT.
     * @return True if the JWT is valid for the specified user, false otherwise.
     */
    
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
    /**
     * Checks whether a given JWT has expired.
     * @param token
     * @return
     */
    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

}
