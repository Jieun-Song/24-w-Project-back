package org.project.exchange.model.user.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignInRequest {

    private String userUsername;
    private String userPassword;

    @Builder
    public SignInRequest(String userUsername, String userPassword) {
        this.userUsername = userUsername;
        this.userPassword = userPassword;
    }

}
