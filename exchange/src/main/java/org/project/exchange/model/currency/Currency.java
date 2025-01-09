package org.project.exchange.model.currency;

import org.project.exchange.model.product.Product;

import jakarta.persistence.*;
import lombok.*;
import java.util.List ;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Entity
@Table(name = "통화")
public class Currency {

    @Id
    @Column(name = "통화_id", length = 255)
    private String currencyId;

    @Column(name = "통화_code", length = 255)
    private String code;

    @Column(name = "통화_exchange_rate", length = 255)
    private String exchangeRate;

    @OneToMany(mappedBy = "currency", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products;
}
