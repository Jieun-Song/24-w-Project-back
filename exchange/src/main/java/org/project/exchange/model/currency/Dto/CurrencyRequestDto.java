package org.project.exchange.model.currency.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.product.Product;

@Getter
@NoArgsConstructor
@Slf4j
public class CurrencyRequestDto {
    private Integer result;
    private String curUnit;
    private String curNm;
    private String ttb;
    private String tts;
    private String dealBasR;
    private String bkpr;
    private String yyEfeeR;
    private String tenDdEfeeR;
    private String kftcDealBasis;
    private String kftcBkpr;

    public Currency toEntity(Currency currency) {
        return Currency.builder()
                .curUnit(this.curUnit)
                .dealBasR(Double.valueOf(this.dealBasR))
                .curNm(this.curNm)
                .build();
    }


}
