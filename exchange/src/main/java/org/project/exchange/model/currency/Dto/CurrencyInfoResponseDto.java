package org.project.exchange.model.currency.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Slf4j
public class CurrencyInfoResponseDto {
    private String curUnit;
    private Double dealBasR;

    public CurrencyInfoResponseDto(String curUnit, Double dealBasR) {
        this.curUnit = curUnit;
        this.dealBasR = dealBasR;
    }
}
