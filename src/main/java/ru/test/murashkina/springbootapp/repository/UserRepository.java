package ru.test.murashkina.springbootapp.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.test.murashkina.springbootapp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

  Optional<User> findByEmail(String email);

  @Override
  Optional<User> findById(UUID uuid);

  Boolean existsByEmail(String email);
}
