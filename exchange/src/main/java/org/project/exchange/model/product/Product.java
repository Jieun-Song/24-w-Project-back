package org.project.exchange.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.list.Lists;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
@Table(name = "product") // 상품 정보 테이블
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false) // 상품 ID
    private Long productId;

    @Column(name = "name", nullable = false, length = 100) // 상품 이름
    private String name;

    @Column(name = "origin_price", nullable = false) // 원래 가격
    private Double originPrice;

    @Column(name = "deleted_yn", nullable = false) // 삭제 여부
    private Boolean deletedYn = false;

    @Column(name = "created_at", nullable = false) // 삭제 여부
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id") // 리스트 ID
    @JsonIgnore
    private Lists lists;

    @Builder
    public Product(String name, LocalDateTime createdAt, Double originPrice, Lists lists) {
        this.name = name;
        this.createdAt = createdAt;
        this.originPrice = originPrice;
        this.lists = lists;
    }
}
