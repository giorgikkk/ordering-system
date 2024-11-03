package com.example.authorizationservice.service;

import com.example.authorizationservice.decorator.SecurityUser;
import com.example.authorizationservice.model.Role;
import com.example.authorizationservice.model.User;
import com.example.authorizationservice.model.enums.RoleType;
import com.example.authorizationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AuthService authService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authService = authService;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role defaultRole = authService.findByName(RoleType.CLIENT);
            user.setRoles(new HashSet<>(Set.of(defaultRole)));
        } else {
            Set<Role> roles = new HashSet<>();
            for (RoleType roleType : user.getRoles().stream().map(Role::getName).toList()) {
                Role role = authService.findByName(roleType);
                roles.add(role);
            }
            user.setRoles(roles);
        }

        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long userId) {
        userRepository.deleteById(userId);
    }

    public User assignSellerRole(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Role sellerRole = authService.findByName(RoleType.SELLER);

        if (user.getRoles().contains(sellerRole)) {
            throw new IllegalStateException("User already has seller role");
        }

        user.getRoles().add(sellerRole);
        return userRepository.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return new SecurityUser(userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
    }
}
