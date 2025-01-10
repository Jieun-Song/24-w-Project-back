package org.project.exchange.model.auth;

import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.user.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "앱권한")
public class Permission {

    @Id
    @Column(name = "앱권한_id", length = 255)
    private String permissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "회원_id", nullable = false)
    private User user;

    @Column(name = "앱권한_type", length = 255)
    private String type;

    @Column(name = "앱권한_status", length = 255)
    private String status;
}
