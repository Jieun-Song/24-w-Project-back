package org.project.exchange.model.list;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.user.User;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "lists") // 리스트 테이블
public class Lists {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "list_id", nullable = false) // 리스트 ID
    private Long listId;

    @Column(name = "name", nullable = false, length = 100) // 리스트 이름
    private String name;

    @Column(name = "location", nullable = false) // 위치
    private String location;

    @Column(name = "deleted_yn", nullable = false) // 삭제 여부
    private Boolean deletedYn = false;

    @OneToMany(mappedBy = "lists", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Product> products; // 해당 리스트의 상품들

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false) // 사용자 ID
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id") // 통화 ID
    @JsonIgnore
    private Currency currency;

    @Builder
    public Lists(String name, String location, User user, Currency currency) {
        this.name = name;
        this.location = location;
        this.products = new ArrayList<>();
        this.user = user;
        this.currency = currency;
    }

    public void setDeletedYn(Boolean deletedYn) {
        this.deletedYn = deletedYn;
    }
}
