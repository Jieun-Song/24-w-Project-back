package org.project.exchange.model.user.Dto;

import org.project.exchange.model.currency.Currency;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED) // ✅ Jackson 직렬화 문제 해결
public class SignUpResponse {

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("userName")
    private String userName;

    @JsonProperty("userEmail")
    private String userEmail;

    @JsonProperty("userGender")
    private boolean userGender;

    @JsonProperty("userDateOfBirth")
    private String userDateOfBirth; // "yyyy-MM-dd" 형식

    @JsonProperty("defaultCurrency")
    private Long defaultCurrencyId;    
}