package com.es.slots.authentication.services;

import com.es.slots.user.entities.User;
import com.es.slots.user.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationUserService implements UserDetailsService {
    
    private final UserRepository userRepository;

    /**
     * Load user by email
     * @param userEmail
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    public UserDetails loadUserByUsername(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
    /**
     * Load user by email
     * @param userEmail
     * @return User
     * @throws UsernameNotFoundException
     */
    public User loadUserByEmail(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Get authenticated user from Spring Security
     * @return User authenticated
     */
    public User getUserAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return loadUserByEmail(userDetails.getUsername());
    }

}
