package ru.test.murashkina.springbootapp.repository;

import ru.test.murashkina.springbootapp.models.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    //TODO DELETE inteface
    @Query(
            value = "SELECT p.name FROM _permission p WHERE p.name LIKE CONCAT('%',:role,'%')",
            nativeQuery = true)
    Collection<String> findAllRolePermissions(@Param("role") String role);
}
