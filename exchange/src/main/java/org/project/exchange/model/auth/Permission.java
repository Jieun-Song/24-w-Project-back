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
@Table(name = "permission") // 앱 권한 테이블
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) // 권한 ID
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 사용자 ID
    private User user;

    @Column(name = "type", nullable = false, length = 50) // 권한 유형
    private String type;

    @Column(name = "status", nullable = false, length = 50) // 권한 상태
    private String status;

    @Builder
    public Permission(Long id, User user, String type, String status) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.status = status;
    }
}
