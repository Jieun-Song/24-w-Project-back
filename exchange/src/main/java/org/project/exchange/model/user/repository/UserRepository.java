package org.project.exchange.model.user.repository;

import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserUsername(String username);

    boolean existsByUserUsername(String username);

    boolean existsByUserEmail(String userEmail);

    Optional<User> findByUserId(Long userId);
}
