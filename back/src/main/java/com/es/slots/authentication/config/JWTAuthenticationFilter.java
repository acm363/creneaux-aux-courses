package com.es.slots.authentication.config;

import com.es.slots.authentication.services.AuthenticationUserService;
import com.es.slots.authentication.services.JWTService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtService;

    private final AuthenticationUserService authUserService;


    /**
     * Implementation of the filter logic. Extracts and validates JWT from the Authorization header
     * and sets up the authentication in the Spring Security context if the token is valid.
     *
     * @param request The {@link HttpServletRequest} object representing the incoming HTTP request.
     * @param response The {@link HttpServletResponse} object representing the HTTP response.
     * @param filterChain The {@link FilterChain} object to invoke the next filter in the chain.
     * @throws ServletException If a servlet-related exception occurs.
     * @throws IOException If an I/O-related exception occurs.
     */

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            final String authorizationHeader = request.getHeader("Authorization");
            final String jwt;
            final String userEmail;

            if (StringUtils.isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 7 = "Bearer ".length()
            jwt = authorizationHeader.substring(7);
            userEmail = jwtService.extractUsername(jwt);

            if (!userEmail.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = authUserService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    SecurityContext securityContextHolder = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    securityContextHolder.setAuthentication(token);
                    SecurityContextHolder.setContext(securityContextHolder);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            // Handle token expiration.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("The token has expired.");
        }
        catch (MalformedJwtException e) {
            // Handle token expiration.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("The token is malformed.");
        }
        catch (Exception e) {
            // Handle token expiration.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("The token is invalid.");
        }
    }
}
