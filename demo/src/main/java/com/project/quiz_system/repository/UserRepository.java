package com.project.quiz_system.repository;

import com.project.quiz_system.entity.User;
import com.project.quiz_system.entity.Role;
import com.project.quiz_system.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User,Long>{
    Optional<User> findByEmail(String email);

    long countByRole(Role role);

    long countByRoleAndStatus(
            Role role,
            Status status
    );
    List<User> findByRole(Role role);
    Optional<User> findByIdAndRole(
            Long id,
            Role role
    );
}

