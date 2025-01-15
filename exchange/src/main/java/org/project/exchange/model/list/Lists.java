package org.project.exchange.model.list;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.user.User;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "lists") // 리스트 테이블
public class Lists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false) // 리스트 ID
    private Long id;

    @Column(name = "name", nullable = false, length = 100) // 리스트 이름
    private String name;

    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products; // 해당 리스트의 상품들

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // 사용자 ID
    private User user;

    @Builder
    public Lists(Long id, String name, List<Product> products, User user) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.user = user;
    }
}
