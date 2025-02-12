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
    @Column(name = "currency_id", nullable = false)
    private Long currencyId;

    @Column(name = "cur_unit", nullable = false, length = 10)
    private String curUnit;

    @Column(name = "deal_das_r", nullable = false)
    private Double dealBasR;

    @Column(name = "cur_nm", nullable = false)
    private String curNm;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

    @Builder
    public Currency(String curUnit, Double dealBasR, String curNm, List<Product> products) {
        this.curUnit = curUnit;
        this.dealBasR = dealBasR;
        this.curNm = curNm;
        this.products = products;
    }
}
