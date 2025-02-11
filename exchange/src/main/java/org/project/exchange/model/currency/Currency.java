package org.project.exchange.model.currency;

import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.product.Product;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "currency")
@Getter
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "currencyId", nullable = false)
    private Long currencyId;

    @Column(name = "curUnit", nullable = false, length = 10)
    private String curUnit;

    @Column(name = "dealBasR", nullable = false)
    private Double dealBasR;

    @Column(name = "curNm", nullable = false)
    private String curNm;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @Builder
    public Currency(String curUnit, Double dealBasR, String curNm) {
        this.curUnit = curUnit;
        this.dealBasR = dealBasR;
        this.curNm = curNm;
    }
}
