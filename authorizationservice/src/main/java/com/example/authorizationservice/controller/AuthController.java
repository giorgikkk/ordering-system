package com.example.authorizationservice.controller;

import com.example.authorizationservice.model.User;
import com.example.authorizationservice.model.dto.JwtResponse;
import com.example.authorizationservice.model.dto.LoginRequest;
import com.example.authorizationservice.model.enums.RoleType;
import com.example.authorizationservice.security.JwtProvider;
import com.example.authorizationservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Autowired
    public AuthController(final AuthenticationManager authenticationManager, final UserService userService, final PasswordEncoder passwordEncoder, final JwtProvider jwtProvider) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/signup")
    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User successfully registered", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Username or email already taken", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> registerUser(@RequestBody final User user) {
        if (userService.findByUsername(user.getUsername()).isPresent() ||
                userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username or email already taken.");
        }

        if (user.getRoles().stream().anyMatch(role -> role.getRole() == RoleType.SELLER)) {
            return ResponseEntity.badRequest().body("Sign up as a SELLER is not allowed.");
        }

        final User savedUser = userService.saveUser(user);
        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/signin")
    @Operation(summary = "Authenticate a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User signed in successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "401", description = "Invalid username or password", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> authenticateUser(@RequestBody final LoginRequest loginRequest) {
        try {
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (passwordEncoder.matches(loginRequest.getPassword(), userDetails.getPassword())) {
                final String jwt = jwtProvider.generateToken(authentication);
                return ResponseEntity.ok(new JwtResponse(jwt));
            } else {
                return ResponseEntity.badRequest().body("Invalid username or password.");
            }
        } catch (final AuthenticationException e) {
            return ResponseEntity.badRequest().body("Invalid username or password.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @GetMapping("/users")
    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> getAllUsers() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        if (authentication.getAuthorities().stream()
                .noneMatch(auth -> auth.getAuthority().equals(RoleType.ADMINISTRATOR.name()))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only administrators can view all users details");
        }

        List<User> allUsers = userService.getAllUsers();
        return ResponseEntity.ok(allUsers);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or authentication.principal.username == #username")
    @GetMapping("/me")
    @Operation(summary = "Get details of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details retrieved successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> getUserDetails(@RequestParam(required = false) final String username) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(RoleType.ADMINISTRATOR.name())) && username != null) {
            final Optional<User> user = userService.findByUsername(username);
            if (user.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            return ResponseEntity.ok(user);
        }

        if (username != null && !username.equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Clients can only view their own information");
        }

        final Optional<User> user = userService.findByUsername(userDetails.getUsername());
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or authentication.name == #userUpdate.username")
    @PutMapping("/me")
    @Operation(summary = "Update details of the currently authenticated user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User details updated successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "400", description = "Invalid user data", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> updateUserDetails(@RequestBody final User userUpdate) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(RoleType.ADMINISTRATOR.name())) && userUpdate.getUsername() != null) {
            final Optional<User> optionalUser = userService.findByUsername(userUpdate.getUsername());
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            final User user = optionalUser.get();
            user.setEmail(userUpdate.getEmail());
            if (userUpdate.getRoles() != null) {
                user.setRoles(userUpdate.getRoles());
            }
            userService.saveUser(user);
            return ResponseEntity.ok("User updated successfully");
        }

        if (userUpdate.getUsername() != null && !userUpdate.getUsername().equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Clients can only update their own information");
        }

        final Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        final User user = optionalUser.get();
        user.setEmail(userUpdate.getEmail());
        userService.saveUser(user);
        return ResponseEntity.ok("User updated successfully");
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or authentication.principal.username == #username")
    @DeleteMapping("/me")
    @Operation(summary = "Delete the currently authenticated user's account")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User account deleted successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> deleteUserAccount(@RequestParam(required = false) final String username) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
        }

        if (authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals(RoleType.ADMINISTRATOR.name())) && username != null) {
            final Optional<User> optionalUser = userService.findByUsername(username);
            if (optionalUser.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            userService.deleteUser(optionalUser.get().getId());
            return ResponseEntity.ok("User deleted successfully");
        }

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        if (username != null && !username.equals(userDetails.getUsername())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Clients can only delete their own accounts");
        }

        final Optional<User> optionalUser = userService.findByUsername(userDetails.getUsername());
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        userService.deleteUser(optionalUser.get().getId());
        return ResponseEntity.ok("User deleted successfully");
    }

    @PreAuthorize("hasRole('ADMINISTRATOR')")
    @PutMapping("/admin/assign-seller/{username}")
    @Operation(summary = "Assign the seller role to a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Seller role assigned successfully", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "User not found", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "500", description = "Server error", content = {@Content(mediaType = "application/json")})
    })
    public ResponseEntity<?> assignSellerRole(@PathVariable final String username) {
        try {
            final User updatedUser = userService.assignSellerRole(username);
            return ResponseEntity.ok(updatedUser);
        } catch (final Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
