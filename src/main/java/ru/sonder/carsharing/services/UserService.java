package ru.sonder.carsharing.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.sonder.carsharing.DTOs.UserDTO;
import ru.sonder.carsharing.models.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    User add(UserDTO userDTO) throws Exception;
    User getUser(String username);
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
    List<User> getTopUsers();
}
