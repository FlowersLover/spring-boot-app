package ru.test.murashkina.springbootapp.models;

import ru.test.murashkina.springbootapp.security.services.RoleService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public enum ERole {
    VIEWER(
            Set.of("UPDATE_USER",
                    "UPDATE_PASSWORD",
                    "READ_USER"
            )
    ),
    EMPLOYEE(
            Set.of("UPDATE_USER",
                    "UPDATE_PASSWORD",
                    "READ_USERS",
                    "READ_USER"
            )
    ),
    ADMIN(
            Set.of("CREATE_USER",
                    "UPDATE_USER",
                    "UPDATE_PASSWORD",
                    "UPDATE_ROLE",
                    "UPDATE_STATUS",
                    "READ_USERS",
                    "READ_USER",
                    "READ_ROLES",
                    "DELETE_USER"
            )
    );

    private RoleService roleService;
    @Getter
    private final Set<String> permissions;

    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission))
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
