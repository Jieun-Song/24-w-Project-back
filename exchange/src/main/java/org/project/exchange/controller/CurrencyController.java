package org.project.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.Dto.CurrencyInfoResponseDto;
import org.project.exchange.model.currency.Dto.CurrencyResponseDto;
import org.project.exchange.model.currency.service.CurrencyService;
import org.project.exchange.model.product.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/info/{curUnit}")
    public ResponseEntity<ApiResponse<CurrencyInfoResponseDto>> getDealBasR(@PathVariable String curUnit) {
        CurrencyInfoResponseDto responseDto = currencyService.getDealBasR(curUnit);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(responseDto, "통화 환율 조회 성공"));
    }
}
