package org.project.exchange.model.auth;

import org.project.exchange.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "시스템로그")
public class SystemLog {

    @Id
    @Column(name = "시스템로그_id", length = 255)
    private String logId;

    @Column(name = "시스템로그_action", length = 255)
    private String action;

    @Column(name = "시스템로그_timestamp", length = 255)
    private String timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "회원_id", nullable = false)
    private User user;
}