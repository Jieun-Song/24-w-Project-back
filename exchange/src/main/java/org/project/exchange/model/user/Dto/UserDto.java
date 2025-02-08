package org.project.exchange.model.user.Dto;

import java.sql.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserDto {
    private Long userId;
    private String userName;
    private Date userDateOfBirth;
    private boolean userGender;
    private String userEmail;

    @Builder
    public UserDto(Long userId, String userName, Date userDateOfBirth, boolean userGender,
            String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userDateOfBirth = userDateOfBirth;
        this.userGender = userGender;
        this.userEmail = userEmail;

    }
}
