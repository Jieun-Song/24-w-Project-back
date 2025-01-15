package org.project.exchange.model.currency;

import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.product.Product;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "code", nullable = false, length = 10)
    private String code;

    @Column(name = "exchange_rate", nullable = false)
    private Double exchangeRate;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @Builder
    public Currency(Long id, String code, Double exchangeRate) {
        this.id = id;
        this.code = code;
        this.exchangeRate = exchangeRate;
    }
}
