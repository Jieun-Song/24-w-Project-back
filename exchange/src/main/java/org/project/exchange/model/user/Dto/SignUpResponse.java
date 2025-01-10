package org.project.exchange.model.user.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponse {

    private final String msg;

    @Builder
    public SignUpResponse(String msg) {
        this.msg = msg;
    }
}