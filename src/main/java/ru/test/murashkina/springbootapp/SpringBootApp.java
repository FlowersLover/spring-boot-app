package ru.test.murashkina.springbootapp;

import ru.test.murashkina.springbootapp.models.*;
import ru.test.murashkina.springbootapp.payload.request.PermissionRequest;
import ru.test.murashkina.springbootapp.payload.request.RoleRequest;
import ru.test.murashkina.springbootapp.payload.request.SignupRequest;
import ru.test.murashkina.springbootapp.repository.RoleRepository;
import ru.test.murashkina.springbootapp.security.services.RoleService;
import ru.test.murashkina.springbootapp.security.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpringBootApp {
	private final RoleRepository roleRepository;

	public SpringBootApp(RoleRepository roleRepository) {
		this.roleRepository = roleRepository;
	}

	public static void main(String[] args) {

		SpringApplication.run(SpringBootApp.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			RoleService service, UserService userService) {
		return args -> {

			Set<String> roleNames = Set.of("ADMIN",
					"VIEWER", "EMPLOYEE"
			);
			for (String roleName : roleNames
			) {
				var role = RoleRequest.builder()
						.name(roleName)
						.build();
				service.saveRole(role);
			}

			Set<String> permissionNames = new HashSet<>();
			permissionNames.addAll(ERole.ADMIN.getPermissions());

			for (String permissionName : permissionNames
			) {
				var permission = PermissionRequest.builder()
						.name(permissionName)
						.build();
				service.savePermission(permission);
			}
			SignupRequest request = SignupRequest.builder()
					.email("admin@mail.ru")
					.name("admin")
					.password("admin")
					.role(roleRepository.findByName(ERole.ADMIN).get().getId().toString())
					.familyName("admin")
					.build();
			userService.register(request);
		};
	}
}

