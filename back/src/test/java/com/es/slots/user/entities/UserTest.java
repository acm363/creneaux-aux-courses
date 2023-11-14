package com.es.slots.user.entities;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserTest {

    public Client client;
    public Admin admin;

    @BeforeAll
    public void setUp() {
        admin = new Admin();
        client = new Client();
    }

    @Test
    void testGetId() {
        Long id = 1L;
        client.setId(id);
        assertEquals(id, client.getId());
    }

    @Test
    void testGetPublicId() {
        String publicId = "publicId";
        client.setPublicId(publicId);
        assertEquals(publicId, client.getPublicId());
    }

    @Test
    void testGetName() {
        String name = "name";
        client.setName(name);
        assertEquals(name, client.getName());
    }

    @Test
    void testGetEmail() {
        String email = "email";
        client.setEmail(email);
        assertEquals(email, client.getEmail());
    }

    @Test
    void testGetPassword() {
        String password = "password";
        client.setPassword(password);
        assertEquals(password, client.getPassword());
    }

    @Test
    void testGetAuthorities() {
        assertEquals(1, client.getAuthorities().size());
    }

    @Test
    void testGetUsername() {
        String email = "email";
        client.setEmail(email);
        assertEquals(email, client.getUsername());
    }

    @Test
    void testIsAccountNonExpired() {
        assertTrue(client.isAccountNonExpired());
    }

    @Test
    void testIsAccountNonLocked() {
        assertTrue(client.isAccountNonLocked());
    }

    @Test
    void testIsCredentialsNonExpired() {
        assertTrue(client.isCredentialsNonExpired());
    }

    @Test
    void testIsEnabled() {
        assertTrue(client.isEnabled());
    }

    @Test
    void testGetAdmin() {
        assertEquals("ADMIN", admin.getType());
    }

    @Test
    void testGetClient() {
        assertEquals("CLIENT", client.getType());
    }

    @Test
    void testToString() {
        String string = "User(id=1, publicId=publicId, name=null, email=email, password=password)";
        assertEquals(string, client.toString());
    }

    @Test
    void testDefaultPickUpOrderList() {
        assertEquals(0, client.getPickUpOrderList().size());
    }

}