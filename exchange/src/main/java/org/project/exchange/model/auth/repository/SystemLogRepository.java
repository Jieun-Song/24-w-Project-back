package org.project.exchange.model.auth.repository;

import org.project.exchange.model.auth.SystemLog;
import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemLogRepository extends JpaRepository<SystemLog, Long> {
    
    void deleteAllByUser(User user);
}
