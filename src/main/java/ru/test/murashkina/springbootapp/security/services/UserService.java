package ru.test.murashkina.springbootapp.security.services;

import ru.test.murashkina.springbootapp.models.Role;
import ru.test.murashkina.springbootapp.models.Status;
import ru.test.murashkina.springbootapp.models.User;
import ru.test.murashkina.springbootapp.payload.request.EditUserInfoRequest;
import ru.test.murashkina.springbootapp.payload.request.SignupRequest;
import ru.test.murashkina.springbootapp.payload.request.UpdatePassswordRequest;
import ru.test.murashkina.springbootapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UserService {
    @Autowired
    PasswordEncoder encoder;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
    }

    public User register(SignupRequest signUpRequest) {
        Role role = roleService.getRoleById(signUpRequest.getRole());
        User user = new User(
                signUpRequest.getName(),
                signUpRequest.getFamilyName(),
                signUpRequest.getMiddleName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), Status.Active, role, new Date());
        userRepository.save(user);
        return user;
    }

    public void updateStatus(Status status, User user) {
        userRepository.delete(user);
        user.setStatus(status);
        userRepository.save(user);
    }

    public void updateRole(Role role, User user) {
        userRepository.delete(user);
        user.setRole(role);
        userRepository.save(user);
    }

    public void updatePassword(UpdatePassswordRequest request, User user) {
        userRepository.delete(user);
        user.setPassword(encoder.encode(request.getPassword()));
        userRepository.save(user);
    }

    public void updateUser(EditUserInfoRequest request, User user) {
        userRepository.delete(user);
        user.setName(request.getName());
        user.setFamilyName(request.getFamilyName());
        user.setMiddleName(request.getMiddleName());
        user.setEmail(request.getEmail());
        userRepository.save(user);
    }

    public User getUserById(String user_id) {
        List<User> users = userRepository.findAll();
        for (User user : users
        ) {
            if (user.getId().toString().equals(user_id)) {
                return user;
            }
        }
        return null;
    }
}
