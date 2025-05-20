package org.project.exchange.model.user.Dto;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindPasswordRequest {
    private String userEmail;
    private String userName;
    private String inputOtp ;


    @Builder
    public FindPasswordRequest(String userEmail, String userName) {
        this.userEmail = userEmail;
        this.userName = userName;
    }
    
}
