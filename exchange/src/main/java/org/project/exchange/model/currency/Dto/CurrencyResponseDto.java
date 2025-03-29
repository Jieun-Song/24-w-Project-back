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
    private Long currencyId;
    @JsonProperty("cur_unit")
    private String curUnit;
    @JsonProperty("deal_bas_r")
    private String dealBasR;
    @JsonProperty("cur_nm")
    private String curNm;

    public CurrencyResponseDto(Currency currency) {
        this.currencyId = currency.getCurrencyId();
        this.curUnit = currency.getCurUnit();
        this.dealBasR = String.valueOf(currency.getDealBasR());
        this.curNm = currency.getCurNm();
    }


}
