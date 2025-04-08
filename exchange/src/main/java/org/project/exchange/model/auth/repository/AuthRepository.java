package org.project.exchange.model.auth.repository;

import java.util.List;

import org.project.exchange.model.auth.Auth;
import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {

    // 🔹 특정 사용자에 대한 모든 인증 정보 조회
    List<Auth> findByUser(User user);

    // 🔹 특정 사용자에 대한 모든 인증 정보 삭제
    void deleteAllByUser(User user);
}
