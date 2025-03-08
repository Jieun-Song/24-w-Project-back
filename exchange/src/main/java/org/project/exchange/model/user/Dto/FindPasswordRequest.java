package org.project.exchange.model.user.Dto;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordRequest {
    private String userEmail;
    private String userName;
    private LocalDate userDateOfBirth;
    private String inputOtp ;


    @Builder
    public FindPasswordRequest(String userEmail, String userName, LocalDate userDateOfBirth) {
        this.userEmail = userEmail;
        this.userName = userName;
        this.userDateOfBirth = userDateOfBirth;
    }
    
}
