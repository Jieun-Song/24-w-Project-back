package org.project.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.Dto.CurrencyResponseDto;
import org.project.exchange.model.currency.service.CurrencyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RestController
@RequestMapping("/api/currency")
@RequiredArgsConstructor
public class CurrencyController {
    private final CurrencyService currencyService;

    /**
     * 1. 처음 통화 받아서 저장(일단위, 주말이면 평일 기준, 현재 시간이 11시 이전이면 어제 기준)
     * 2. 모든 통화 조회
     * 3. 통화 코드를 가지고 특정 통화 조회
     */
    @PostMapping("/import")
    public List<Currency> importCurrency() {
        return currencyService.fetchAndSaveCurrency();
    }

    @GetMapping
    public List<CurrencyResponseDto> findAll() {
        return currencyService.findAllCurrency();
    }
}
