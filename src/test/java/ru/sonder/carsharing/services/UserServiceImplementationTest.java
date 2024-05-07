package ru.sonder.carsharing.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.sonder.carsharing.DTOs.UserDTO;
import ru.sonder.carsharing.models.Role;
import ru.sonder.carsharing.models.User;
import ru.sonder.carsharing.repositories.UserRepository;
import ru.sonder.carsharing.services.implementations.UserServiceImplementation;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplementationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImplementation userService;

    @Test
    void testAddExistingUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("existingUser");
        userDTO.setPassword("password");

        when(userRepository.getByUsername("existingUser")).thenReturn(new User());

        assertThrows(Exception.class, () -> userService.add(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByUsername() {
        User testUser = new User();
        testUser.setUsername("testUser");

        when(userRepository.getByUsername("testUser")).thenReturn(testUser);

        User retrievedUser = userService.getUser("testUser");

        assertNotNull(retrievedUser);
        assertEquals("testUser", retrievedUser.getUsername());
    }

    @Test
    void testLoadUserByUsername() {
        User testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("hashedPassword");
        testUser.setRoles(List.of(new Role("ROLE_ADMIN")));

        when(userRepository.getByUsername("testUser")).thenReturn(testUser);

        UserDetails userDetails = userService.loadUserByUsername("testUser");

        assertNotNull(userDetails);
        assertEquals("testUser", userDetails.getUsername());
        assertEquals("hashedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().containsAll(mapRolesToAuthorities(testUser.getRoles())));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .toList();
    }
}
