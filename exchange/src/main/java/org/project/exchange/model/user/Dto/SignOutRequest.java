package org.project.exchange.model.user.Dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class SignOutRequest {

    private String userEmail;

    @Builder
    public SignOutRequest(String userEmail) {
        this.userEmail = userEmail;
    }

}