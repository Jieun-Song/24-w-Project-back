package org.project.exchange.model.user.Dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ResetNameResponse {
    String userEmail ;
    String newName ;
    String msg ;

    @Builder
    public ResetNameResponse(String userEmail, String userName, String msg) {
        this.userEmail = userEmail;
        this.newName = userName;
        this.msg = msg;
    }


}
