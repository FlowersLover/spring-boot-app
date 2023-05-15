package ru.test.murashkina.springbootapp.controllers;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


import ru.test.murashkina.springbootapp.models.Status;
import ru.test.murashkina.springbootapp.payload.response.*;
import ru.test.murashkina.springbootapp.payload.response.errors.BasicMassegResponse;
import ru.test.murashkina.springbootapp.payload.response.errors.FieldsResponse;
import ru.test.murashkina.springbootapp.payload.response.errors.ErrResponse;
import ru.test.murashkina.springbootapp.payload.response.errors.MessageResponse;
import ru.test.murashkina.springbootapp.security.services.RoleService;
import ru.test.murashkina.springbootapp.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ru.test.murashkina.springbootapp.models.Role;
import ru.test.murashkina.springbootapp.models.User;
import ru.test.murashkina.springbootapp.payload.request.LoginRequest;
import ru.test.murashkina.springbootapp.payload.request.SignupRequest;
import ru.test.murashkina.springbootapp.repository.RoleRepository;
import ru.test.murashkina.springbootapp.repository.UserRepository;
import ru.test.murashkina.springbootapp.security.jwt.JwtUtils;
import ru.test.murashkina.springbootapp.security.services.UserDetailsImpl;

@RestController
@AllArgsConstructor
public class AuthController {
    private final RoleService roleService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationManager authenticationManager;

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles));
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(new FieldsResponse(new ErrResponse("Пользователь с таким же e-mail уже существует",
                            "EmailExists",
                            signUpRequest.getEmail()
                    ))));
        }
        Role role = roleService.getRoleById(signUpRequest.getRole());
        if (role == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new BasicMassegResponse("Указаная роль не существует"));
        }
        //создаем нового польсователя
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getFamilyName(),
                signUpRequest.getMiddleName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), Status.Active, role, new Date());
        userRepository.save(user);
        return ResponseEntity.ok(new SignupResponse(
                user.getId(),
                user.getEmail(),
                user.getFamilyName(),
                user.getName(),
                user.getMiddleName(),
                user.getRole().getId().toString(),
                user.getStatus().name(),
                user.getCreatedAt().toString()));
    }
}
