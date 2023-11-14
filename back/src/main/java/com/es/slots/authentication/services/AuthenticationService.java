package com.es.slots.authentication.services;

import com.es.slots.authentication.dto.JwtLoginResponse;
import com.es.slots.authentication.dto.LoginRequest;
import com.es.slots.user.entities.User;
import com.es.slots.user.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    private final JWTService jwtService;

    /**
     * Login user
     * @param logInRequest
     * @return
     */
    public JwtLoginResponse login(LoginRequest logInRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInRequest.getEmail(), logInRequest.getPassword()));

        User user = userRepository.findByEmail(logInRequest.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
        String jwt = jwtService.generateToken(user);

        JwtLoginResponse jwtLoginResponse = new JwtLoginResponse();
        jwtLoginResponse.setToken(jwt);
        return jwtLoginResponse;
    }

}
