package ru.test.murashkina.springbootapp.controllers;

import ru.test.murashkina.springbootapp.models.Role;
import ru.test.murashkina.springbootapp.models.Status;
import ru.test.murashkina.springbootapp.models.User;
import ru.test.murashkina.springbootapp.payload.request.EditUserInfoRequest;
import ru.test.murashkina.springbootapp.payload.request.UpdatePassswordRequest;
import ru.test.murashkina.springbootapp.payload.request.UpdateRoleRequest;
import ru.test.murashkina.springbootapp.payload.response.*;
import ru.test.murashkina.springbootapp.payload.response.errors.*;
import ru.test.murashkina.springbootapp.repository.UserRepository;
import ru.test.murashkina.springbootapp.security.services.RoleService;
import ru.test.murashkina.springbootapp.security.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminController {

    private final RoleService roleService;

    private final UserRepository userRepository;

    private final UserService userService;

    @GetMapping("/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") String userId) {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound",
                            "Пользователь с идентификатором " + userId + " не найден"));
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editUserInfoAdmin(@PathVariable("userId") String userId, @Valid @RequestBody EditUserInfoRequest request) {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound",
                            "Пользователь с идентификатором " + userId + " не найден"));
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> editUserPasswordAdmin(@PathVariable("userId") String userId,
                                                   @Valid @RequestBody UpdatePassswordRequest request) {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound", "Пользователь с идентификатором " + userId + " не найден"));

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

    @PutMapping(path = "{userId}/set-role")
    @PreAuthorize("hasAuthority('UPDATE_ROLE')")
    public ResponseEntity<?> editUserRoleAdmin(@PathVariable("userId") String userId,
                                               @Valid @RequestBody UpdateRoleRequest request) {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound",
                            "Пользователь с идентификатором " + userId + " не найден"));
        }

        Role role = roleService.getRoleById(request.getRole());
        if (role == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageRoleResponse(new RoleErr(new ErrResponse("Указанная роль не существует",
                            "RoleNotFound",
                            request.getRole()
                    ))));
        }
        userService.updateRole(role, currentUser);
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

    @PutMapping(path = "{userId}/{state}")
    @PreAuthorize("hasAuthority('UPDATE_STATUS')")
    public ResponseEntity<?> editUserRoleAdmin(@PathVariable("userId") String userId, @PathVariable("state") String state) {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound", "Пользователь с идентификатором " + userId + " не найден"));
        }
        Status status = Status.valueOf(state);
        userService.updateStatus(status, currentUser);
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

    @DeleteMapping(path = "{userId}")
    @PreAuthorize("hasAuthority('DELETE_USER')")
    public ResponseEntity<?> editUserRoleAdmin(@PathVariable("userId") String userId) {
        User currentUser = this.userService.getUserById(userId);
        if (currentUser == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new CodeResponse("UserNotFound", "Пользователь с идентификатором " + userId + " не найден"));
        }

        userRepository.delete(currentUser);
        return ResponseEntity.ok().body("Пользователь с идентификатором" + userId + "удален.");
    }

}
