package org.project.exchange.model.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class KakaoUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "kakao_user_id", nullable = false)
    private Long kakaoUserId;

    @Column(name = "kakao_id", nullable = false, unique = true)
    private String kakaoId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "access_token")
    private String accessToken;

    @Builder
    public KakaoUser(String kakaoId, String nickname, User user, String accessToken) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.user = user;
        this.accessToken = accessToken;

    }
}