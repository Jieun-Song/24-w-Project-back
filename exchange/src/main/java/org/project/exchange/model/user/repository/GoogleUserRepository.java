package org.project.exchange.model.user.repository;

import java.util.Optional;

import org.project.exchange.model.user.GoogleUser;
import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoogleUserRepository extends JpaRepository<GoogleUser, Long> {
    Optional<GoogleUser> findByUser(User user);
    boolean existsByUserUserEmail(String userEmail);
}
