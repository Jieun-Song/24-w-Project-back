package org.project.exchange.model.user.Dto;

import java.sql.Date;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
public class SignUpRequest {

    private final String userName;
    private final Date userDateOfBirth;
    private final boolean userGender;
    private final String userEmail;
    private final String userPassword;
    private String otp;
    private List<Boolean> agreedTerms;

    @Builder
    public SignUpRequest(String userName, Date userDateOfBirth, boolean userGender,
            String userEmail,  String userPassword, String otp, List<Boolean> agreedTerms) {
        this.userName = userName;
        this.userDateOfBirth = userDateOfBirth;
        this.userGender = userGender;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.otp = otp;
        this.agreedTerms = agreedTerms;
    }
}
