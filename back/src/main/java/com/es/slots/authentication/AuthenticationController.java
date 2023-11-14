package com.es.slots.authentication;

import com.es.slots.authentication.dto.JwtLoginResponse;
import com.es.slots.authentication.dto.LoginRequest;
import com.es.slots.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins="http://localhost:3000")
@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Handles POST requests for user login.
     * Expects a {@link LoginRequest} in the request body and returns a {@link JwtLoginResponse} containing the JWT token.
     *
     * @param logInRequest The {@link LoginRequest} containing user credentials.
     * @return A {@link ResponseEntity} containing a {@link JwtLoginResponse}.
     */
    @PostMapping("/login")
    public ResponseEntity<JwtLoginResponse> login(@RequestBody LoginRequest logInRequest) {
        return ResponseEntity.ok(authenticationService.login(logInRequest));
    }

    /**
     * Handles GET requests to retrieve information about the authenticated user.
     * Returns the {@link Authentication} object representing the current user's authentication details.
     *
     * @return The {@link Authentication} object representing the authenticated user.
     */
    @GetMapping("/me")
    public Authentication me() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Handles GET requests for a demonstration endpoint accessible only to users with the 'ADMIN' authority.
     * Uses {@link PreAuthorize} annotation for role-based access control.
     *
     * @return A greeting string for the 'ADMIN' user.
     */
    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/admin/hello")
    public String helloAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "Hello from " + authentication.toString();
    }

    /**
     * Handles GET requests for a demonstration endpoint accessible only to users with the 'CLIENT' authority.
     * Uses {@link PreAuthorize} annotation for role-based access control.
     *
     * @return A greeting string for the 'CLIENT' user.
     */
    @PreAuthorize("hasAuthority('CLIENT')")
    @GetMapping("/client/hello")
    public String helloClient() {
        return "Hello from client";
    }

}
