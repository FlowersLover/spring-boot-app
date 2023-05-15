package ru.test.murashkina.springbootapp.controllers;

import ru.test.murashkina.springbootapp.models.User;
import ru.test.murashkina.springbootapp.payload.request.EditUserInfoRequest;
import ru.test.murashkina.springbootapp.payload.request.UpdatePassswordRequest;
import ru.test.murashkina.springbootapp.payload.response.*;
import ru.test.murashkina.springbootapp.payload.response.errors.*;
import ru.test.murashkina.springbootapp.repository.UserRepository;
import ru.test.murashkina.springbootapp.security.services.UserDetailsImpl;
import ru.test.murashkina.springbootapp.security.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class EmployeeController {
    private final UserRepository userRepository;
    private final UserService userService;
    @GetMapping("/{userId}")
    @PreAuthorize("hasAnyRole('VIEWER','EMPLOYEE')")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") String userId) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound",
                            "Пользователь с идентификатором " + userId + " не найден"));
        }
        if (!userId.equals(userDetailsImpl.getId().toString())) {
            throw new RuntimeException("Нет доступа");
        }
        return ResponseEntity.ok(new SignupResponse(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getFamilyName(),
                currentUser.getName(),
                currentUser.getMiddleName(),
                currentUser.getRole().getId().toString(),
                currentUser.getStatus().name(),
                currentUser.getCreatedAt().toString()));
    }

    @PutMapping(path = "/{userId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE','VIEWER')")
    public ResponseEntity<?> editUserInfo(@PathVariable("userId") String userId, @Valid @RequestBody EditUserInfoRequest request) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound",
                            "Пользователь с идентификатором " + userId + " не найден"));
        }
        if (!userId.equals(userDetailsImpl.getId().toString())) {
            throw new RuntimeException("Нет доступа");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(new FieldsResponse(new ErrResponse("Другой пользователь с таким же e-mail уже существует",
                            "AnotherEmailExists",
                            request.getEmail()
                    ))));
        }
        userService.updateUser(request, currentUser);
        return ResponseEntity.ok(new SignupResponse(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getFamilyName(),
                currentUser.getName(),
                currentUser.getMiddleName(),
                currentUser.getRole().getId().toString(),
                currentUser.getStatus().name(),
                currentUser.getCreatedAt().toString()));
    }

    @PutMapping(path = "{userId}/set-password")
    @PreAuthorize("hasAnyRole('EMPLOYEE','VIEWER')")
    public ResponseEntity<?> editUserPassword(@PathVariable("userId") String userId, @Valid @RequestBody UpdatePassswordRequest request) {
        UserDetailsImpl userDetailsImpl = (UserDetailsImpl) SecurityContextHolder
                .getContext().getAuthentication().getPrincipal();
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound",
                            "Пользователь с идентификатором " + userId + " не найден"));
        }
        if (!userId.equals(userDetailsImpl.getId().toString())) {
            throw new RuntimeException("Нет доступа");
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(new FieldsResponse(
                            new ErrResponse("Введенные пароли не совпадают",
                                    "PasswordNotConfirmed",
                                    request.getPassword()),
                            new ErrResponse("Введенные пароли не совпадают",
                                    "PasswordNotConfirmed",
                                    request.getConfirmPassword())
                    )));
        }
        if (!userId.equals(userDetailsImpl.getId().toString())) {
            throw new RuntimeException("Нет доступа");
        }
        userService.updatePassword(request, currentUser);
        return ResponseEntity.ok(new SignupResponse(
                currentUser.getId(),
                currentUser.getEmail(),
                currentUser.getFamilyName(),
                currentUser.getName(),
                currentUser.getMiddleName(),
                currentUser.getRole().getId().toString(),
                currentUser.getStatus().name(),
                currentUser.getCreatedAt().toString()));
    }
}
