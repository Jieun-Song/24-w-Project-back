package org.project.exchange.model.user.repository;

import org.project.exchange.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일을 소문자로 변환하여 검색 (대소문자 구분하지 않음)
    @Query("SELECT u FROM User u WHERE LOWER(u.userEmail) = LOWER(:userEmail)")
    Optional<User> findByUserEmailOptional(@Param("userEmail") String userEmail);

    User findByUserEmail(String userEmail);

    // 중복 검사도 대소문자 구분 없이
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE LOWER(u.userEmail) = LOWER(:userEmail)")
    boolean existsByUserEmail(@Param("userEmail") String userEmail);

    @Query("SELECT u FROM User u WHERE u.userId = :userId")
    Optional<User> findByUserId(@Param("userId") Long userId);

    User findByUserNameAndUserDateOfBirth(String userName, LocalDate userDateOfBirth);

    List<User> findAllByUserNameAndUserDateOfBirth(  String userName,
            LocalDate userDateOfBirth);

    Optional<User> findByUserEmailAndUserNameAndUserDateOfBirth(
            String userEmail,
            String userName,
            LocalDate userDateOfBirth);
}
