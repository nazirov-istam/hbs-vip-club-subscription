package org.example.grand_education.repository;

import org.example.grand_education.enums.Role;
import org.example.grand_education.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findTopByRoleOrderByCreatedAtDesc(Role role);
    List<User> findAllByRole(Role role);
    Optional<User> findByRole(Role role);
    boolean existsByRole(Role role);
    int countAllByRole(Role role);
}