package org.project.exchange.model.user.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignInResponse {
    private Long userId;
    private String userName;
    private String msg;
    private String accessToken;
    private String refreshToken;
    private String kakaoAccessToken;

    @Builder
    public SignInResponse(Long userId, String userName, String msg, String accessToken, String refreshToken,
            String kakaoAccessToken) {
        this.userId = userId;
        this.userName = userName;
        this.msg = msg;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.kakaoAccessToken = kakaoAccessToken;
    }

}
