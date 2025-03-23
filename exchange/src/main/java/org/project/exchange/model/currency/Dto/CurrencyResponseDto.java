package org.project.exchange.model.currency.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.currency.Currency;

@Getter
@NoArgsConstructor
@Slf4j
public class CurrencyResponseDto {
    private Long currentId;
    private String curUnit;
    private String dealBasR;
    private String curNm;

    public CurrencyResponseDto(Currency currency) {
        this.currentId = currency.getCurrencyId();
        this.curUnit = currency.getCurUnit();
        this.dealBasR = String.valueOf(currency.getDealBasR());
        this.curNm = currency.getCurNm();
    }


}
