package org.project.exchange.model.list;

import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.user.User;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "리스트")
public class Lists {

    @Id
    @Column(name = "리스트_id", length = 255)
    private String listId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "회원_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private java.util.List<Product> products;

    @Column(name = "리스트_name", length = 255)
    private String name;

    @Column(name = "리스트_total_amount", length = 255)
    private String totalAmount;

    @Column(name = "리스트_created_at", length = 255)
    private String createdAt;

    @Column(name = "리스트_location", length = 255)
    private String location;
}
