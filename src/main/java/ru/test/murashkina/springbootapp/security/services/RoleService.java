package ru.test.murashkina.springbootapp.security.services;

import ru.test.murashkina.springbootapp.models.ERole;
import ru.test.murashkina.springbootapp.models.Permission;
import ru.test.murashkina.springbootapp.models.Role;
import ru.test.murashkina.springbootapp.payload.request.PermissionRequest;
import ru.test.murashkina.springbootapp.payload.request.RoleRequest;
import ru.test.murashkina.springbootapp.repository.PermissionRepository;
import ru.test.murashkina.springbootapp.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public Role getRoleById(String role_id) {
        List<Role> roles = roleRepository.findAll();
        for (Role role : roles
        ) {
            if (role.getId().toString().equals(role_id)) {
                return role;
            }
        }
        return null;
    }

    public void savePermission(PermissionRequest request) {
        var permission = Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        permissionRepository.save(permission);
    }

    public void saveRole(RoleRequest request) {
        var role = Role.builder()
                .name(ERole.valueOf(request.getName()))
                .build();
        roleRepository.save(role);
    }

    //TODO удалить метод
    public Role getRoleById(UUID id) {
        return roleRepository.findAllById(Collections.singleton(id)).get(0); //реализовали метод внедренного бина
    }

}

