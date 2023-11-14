package com.es.slots.authentication;

import com.es.slots.authentication.dto.JwtLoginResponse;
import com.es.slots.authentication.services.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;


    @Test
    void adminHello_ShouldReturn200_WhenAuthenticated_AsAdmin() throws Exception {
        mockMvc.perform(get("/admin/hello")
                .with(user("admin@gmail.com")
                        .password("admin")
                        .authorities(new SimpleGrantedAuthority("ADMIN"))))
                .andExpect(status().isOk());
    }

    @Test
    void adminHello_ShouldReturn403_WhenAuthenticated_AsClient() throws Exception {
        mockMvc.perform(get("/admin/hello")
                .with(user("client@gmail.com")
                        .password("client")
                        .authorities(new SimpleGrantedAuthority("CLIENT"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void clientHello_ShouldReturn200_WhenAuthenticated_AsClient() throws Exception {
        mockMvc.perform(get("/client/hello")
                .with(user("client@gmail.com")
                        .password("client")
                        .authorities(new SimpleGrantedAuthority("CLIENT")))).andExpect(status().isOk());
    }

    @Test
    void clientHello_ShouldReturn403_WhenAuthenticated_AsAdmin() throws Exception {
        mockMvc.perform(get("/client/hello")
                .with(user("admin@gmail.com")
                        .password("admin")
                        .authorities(new SimpleGrantedAuthority("ADMIN")))).andExpect(status().isForbidden());
    }


    @Test
    void authLogin_shouldReturn200_WithUserInfos() throws Exception {
        JwtLoginResponse jwtLoginResponse = new JwtLoginResponse();
        jwtLoginResponse.setToken("Bearer");

        when(authenticationService.login(any())).thenReturn(jwtLoginResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"admin@gmail.com\", \"password\":\"admin\"}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.token").isNotEmpty());

        verify(authenticationService, times(1)).login(any());
    }


   
    @Test
    void authLogin_shouldReturn401_WithBadCredentials() throws Exception {
        JwtLoginResponse jwtLoginResponse = new JwtLoginResponse();
        jwtLoginResponse.setToken("Bearer");

        when(authenticationService.login(any())).thenThrow(new BadCredentialsException("Invalid email or password"));

        mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"admin@gmail.com\", \"password\":\"fhgbdhbeufbeufbzufgzufz\"}"))
                .andExpect(status().isUnauthorized());

        verify(authenticationService, times(1)).login(any());
    }
}


