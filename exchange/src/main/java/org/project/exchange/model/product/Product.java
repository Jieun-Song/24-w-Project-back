package org.project.exchange.model.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id") // 리스트 ID
    @JsonIgnore
    private Lists lists;

    @Builder
    public Product(String name, Double originPrice, Lists lists) {
        this.name = name;
        this.originPrice = originPrice;
        this.lists = lists;

    }
}
