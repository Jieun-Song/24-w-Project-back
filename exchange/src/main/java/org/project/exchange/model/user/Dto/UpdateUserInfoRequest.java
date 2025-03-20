package org.project.exchange.model.user.Dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserInfoRequest {
    private String userEmail;
    private String userName;
    private String userDateOfBirth; // yyyy-MM-dd 포맷
    private String userPassword;
}
