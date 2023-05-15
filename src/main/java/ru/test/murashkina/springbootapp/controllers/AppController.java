package ru.test.murashkina.springbootapp.controllers;

import ru.test.murashkina.springbootapp.models.User;
import ru.test.murashkina.springbootapp.payload.response.RoleResponse;
import ru.test.murashkina.springbootapp.payload.response.SignupResponse;
import ru.test.murashkina.springbootapp.payload.response.errors.CodeResponse;
import ru.test.murashkina.springbootapp.repository.RoleRepository;
import ru.test.murashkina.springbootapp.repository.UserRepository;
import ru.test.murashkina.springbootapp.security.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class AppController {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final UserService userService;



    @GetMapping("/roles")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    public List<RoleResponse> getAllRoles() {
        List<RoleResponse> roles = roleRepository.findAll()
                .stream()
                .map(role -> new RoleResponse(role.getName().name(), role.getId().toString(), role.getDescription()))
                .collect(Collectors.toList());
        return roles;
    }

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('READ_USERS')")
    public List<SignupResponse> getAllUsers() {
        List<SignupResponse> users = userRepository.findAll()
                .stream()
                .map(user -> new SignupResponse(
                        user.getId(),
                        user.getName(),
                        user.getFamilyName(),
                        user.getMiddleName(),
                        user.getEmail(),
                        user.getRole().getId().toString(),
                        user.getStatus().name(),
                        user.getCreatedAt().toString()
                ))
                .collect(Collectors.toList());
        return users;
    }


}