package org.project.exchange.model.auth;

import org.project.exchange.model.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "auth") // 인증 테이블
public class Auth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) // 인증 ID
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 사용자 ID
    private User user;

    @Column(name = "type", nullable = false, length = 50) // 인증 유형
    private String type;

    @Builder
    public Auth(Long id, User user, String type) {
        this.id = id;
        this.user = user;
        this.type = type;
    }
}
