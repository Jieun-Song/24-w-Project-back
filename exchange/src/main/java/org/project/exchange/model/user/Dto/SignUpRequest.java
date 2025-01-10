package org.project.exchange.model.user.Dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpRequest {

    private final String userName;
    private final Date userDateOfBirth;
    private final String userPhoneNumber;
    private final boolean userGender;
    private final String userEmail;
    private final String userUsername;
    private final String userPassword;

    @Builder
    public SignUpRequest(String userName, Date userDateOfBirth, String userPhoneNumber, boolean userGender,
            String userEmail, String userUsername, String userPassword) {
        this.userName = userName;
        this.userDateOfBirth = userDateOfBirth;
        this.userPhoneNumber = userPhoneNumber;
        this.userGender = userGender;
        this.userEmail = userEmail;
        this.userUsername = userUsername;
        this.userPassword = userPassword;
    }
}
