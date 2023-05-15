package ru.test.murashkina.springbootapp.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.test.murashkina.springbootapp.models.ERole;
import ru.test.murashkina.springbootapp.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
  Optional<Role> findByName(ERole name);

  @Override
  Optional<Role> findById(UUID uuid);

  @Override
  List<Role> findAll();

  @Override
  List<Role> findAllById(Iterable<UUID> uuids);
}
