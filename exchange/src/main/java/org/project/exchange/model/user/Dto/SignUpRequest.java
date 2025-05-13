package org.project.exchange.model.user.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.sql.Date;
import java.util.List;


@Getter
public class SignUpRequest {

    private String userName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private  Date userDateOfBirth;

    private boolean userGender;

    private String userEmail;

    private String userPassword;

    private String otp;
    
    private List<Boolean> agreedTerms;

    private Long defaultCurrencyId;

}
