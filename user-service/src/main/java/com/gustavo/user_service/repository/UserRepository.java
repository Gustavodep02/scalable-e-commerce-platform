package com.gustavo.user_service.repository;

import com.gustavo.user_service.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    UserDetails findByEmail(String email);
    Optional<User> findUserById(UUID id);

    boolean existsByEmailAndIdNot(String email, UUID id);
}
