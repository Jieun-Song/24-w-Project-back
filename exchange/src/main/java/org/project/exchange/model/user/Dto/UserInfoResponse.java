package org.project.exchange.model.user.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private Long userId;
    private String userName;
    private String userEmail;
    private String userDateOfBirth;
    private boolean isKakaoUser;
}
