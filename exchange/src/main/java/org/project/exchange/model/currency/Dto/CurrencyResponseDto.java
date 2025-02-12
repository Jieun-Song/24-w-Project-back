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
    @JsonProperty("cur_unit")
    private String curUnit;
    @JsonProperty("deal_bas_r")
    private String dealBasR;
    @JsonProperty("cur_nm")
    private String curNm;

    public CurrencyResponseDto(Currency currency) {
        this.currentId = currency.getCurrencyId();
        this.curUnit = currency.getCurUnit();
        this.dealBasR = String.valueOf(currency.getDealBasR());
        this.curNm = currency.getCurNm();
    }

    // CurrencyResponseDto에서 Currency 엔티티로 변환
    public Currency toEntity() {
        return Currency.builder()
                .curUnit(curUnit)
                .dealBasR(getParsedDealBasR())
                .curNm(curNm)
                .build();
    }
    public Double getParsedDealBasR() {
        try {
            return Double.valueOf(dealBasR.replace(",", ""));
        } catch (NumberFormatException e) {
            log.error("Failed to parse dealBasR: {}", dealBasR, e);
            return 0.0; // 기본값 처리
        }
    }
}
