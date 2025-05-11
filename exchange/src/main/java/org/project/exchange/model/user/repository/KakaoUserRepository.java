package org.project.exchange.model.user.repository;

import org.project.exchange.model.user.KakaoUser;
import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoUserRepository extends JpaRepository<KakaoUser, Long> {
    Optional<KakaoUser> findByKakaoId(String kakaoId);
    
    Optional<KakaoUser> findByUser(User user);

}