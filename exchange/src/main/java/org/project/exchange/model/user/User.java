package org.project.exchange.model.user;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.sql.Date;
import java.time.LocalDate;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToMany;
import org.project.exchange.model.auth.Auth;
import org.project.exchange.model.auth.Permission;
import org.project.exchange.model.auth.SystemLog;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId; // 비식별자 id (자동 증가)

    @Column(name = "user_name", nullable = false, length = 50)
    private String userName; // 사용자 이름

    @Column(name = "user_date_of_birth", nullable = false)
    private Date userDateOfBirth; // 생년월일

    @Column(name = "user_gender", nullable = false, length = 1)
    private boolean userGender; // 성별

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail; // 이메일

    @Column(name = "user_password", nullable = false, length = 255)
    private String userPassword; // 비밀번호

    @Column(name = "user_created_at", nullable = false)
    private Date userCreatedAt; // 생성일시

    @Column(name = "user_updated_at", nullable = false)
    private Date userUpdatedAt; // 수정일시

    // Relationships
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Auth> auths; // 인증 정보

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Permission> permissions; // 권한 정보

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SystemLog> systemLogs; // 시스템 로그

    @JsonManagedReference
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lists> lists; // 사용자가 생성한 리스트들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "default_currency_id")
    private Currency defaultCurrency; // 사용자가 설정한 기본 통화

    @Builder(toBuilder = true)
public User(Long userId, String userName, Date userDateOfBirth,
            boolean userGender, String userEmail, String userPassword,
            Date userCreatedAt, Date userUpdatedAt, Currency defaultCurrency) {
    this.userId = userId;
    this.userName = userName;
    this.userDateOfBirth = userDateOfBirth;
    this.userGender = userGender;
    this.userEmail = userEmail;
    this.userPassword = userPassword;
    this.userCreatedAt = userCreatedAt != null ? userCreatedAt : Date.valueOf(LocalDate.now());
    this.userUpdatedAt = userUpdatedAt;
    this.defaultCurrency = defaultCurrency;
}

}