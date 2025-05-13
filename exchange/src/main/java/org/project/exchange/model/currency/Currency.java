package org.project.exchange.model.currency;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @OneToMany(mappedBy = "defaultCurrency")
    @JsonIgnore
    private List<User> usersWithThisAsDefault;

    @Builder
    public Currency(String curUnit, Double dealBasR, String curNm, LocalDate createdAt) {
        this.curUnit = curUnit;
        this.dealBasR = dealBasR;
        this.curNm = curNm;
        this.createdAt = createdAt;
    }

    public void updateDealBasR(Double dealBasR) {
            this.dealBasR = dealBasR;
    }

    public void updateCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }
}
