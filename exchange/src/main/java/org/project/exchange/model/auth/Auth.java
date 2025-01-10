package org.project.exchange.model.auth;

import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.user.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "인증")
public class Auth {

    @Id
    @Column(name = "인증_id", length = 255)
    private String authId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "회원_id", nullable = false)
    private User user;

    @Column(name = "인증_type", length = 255)
    private String authType;

    @Column(name = "인증_field", length = 255)
    private String authField;
}
