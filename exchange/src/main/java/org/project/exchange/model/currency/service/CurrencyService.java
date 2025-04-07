package org.project.exchange.model.currency.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.config.CurrencyApiProperties;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.currency.Dto.CurrencyInfoResponseDto;
import org.project.exchange.model.currency.Dto.CurrencyResponseDto;
import org.project.exchange.model.currency.repository.CurrencyRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CurrencyService {
    private final CurrencyRepository currencyRepository;
    private final WebClient webClient;
    private final CurrencyApiProperties currencyApiProperties;

    public List<Currency> fetchAndSaveCurrency() {
        LocalDate myDate = LocalDate.now();
        LocalTime myTime = LocalTime.now();
        DayOfWeek dayOfWeek = myDate.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SATURDAY || myTime.isBefore(LocalTime.of(11, 0))) {
            myDate = myDate.minusDays(1);
        } else if (dayOfWeek == DayOfWeek.SUNDAY) {
            myDate = myDate.minusDays(2);
        }
        String formatedNow = myDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String url = String.format("https://www.koreaexim.go.kr/site/program/financial/exchangeJSON?data=AP01&authkey=%s&searchdate=%s",
                currencyApiProperties.getKey(),
                formatedNow);
        log.info(url);
        //공공데이터 API에서 JSON 데이터 가져오기
        List<CurrencyResponseDto> responseDtoList = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<CurrencyResponseDto>>() {})
                .timeout(Duration.ofSeconds(15))
                .onErrorResume(e -> {
                    log.error("API 호출 실패: {}", e.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .block();

        if (responseDtoList == null || responseDtoList.isEmpty()) {
            log.error("API 응답이 비어있습니다. URL: {}", url);
            throw new RuntimeException("API 응답이 비어있습니다.");
        }

        for (CurrencyResponseDto dto : responseDtoList) {
            if (dto.getDealBasR() == null || dto.getCurUnit() == null || dto.getCurNm() == null) {
                log.warn("불완전한 데이터가 있습니다: dealBasR={}, curUnit={}, curNm={}", dto.getDealBasR(), dto.getCurUnit(), dto.getCurNm());
            }
        }
        List<Currency> savedList = responseDtoList.stream()
                .map(dto -> {
                    Currency currency = currencyRepository.findByCurUnit(dto.getCurUnit())
                            .map(existing -> {
                                // 이미 존재하면 금액만 업데이트
                                existing.updateDealBasR(getParsedDealBasR(dto.getDealBasR()));
                                return existing;
                            })
                            .orElseGet(() -> {
                                // 없으면 새로 저장할 Currency 객체 생성
                                return Currency.builder()
                                        .curUnit(dto.getCurUnit())
                                        .dealBasR(getParsedDealBasR(dto.getDealBasR()))
                                        .curNm(dto.getCurNm())
                                        .build();
                            });
                    return currency;
                })
                .collect(Collectors.toList());

        return currencyRepository.saveAll(savedList);
    }

    public List<CurrencyResponseDto> findAllCurrency(){
        List<Currency> currencies = currencyRepository.findAll();
        return currencies.stream()
                .map(CurrencyResponseDto::new)
                .collect(Collectors.toList());
    }

    public Double getParsedDealBasR(String dealBasR) {
        try {
            return Double.valueOf(dealBasR.replace(",", ""));
        } catch (NumberFormatException e) {
            log.error("Failed to parse dealBasR: {}", dealBasR, e);
            return 0.0; // 기본값 처리
        }
    }

    public CurrencyInfoResponseDto getDealBasR(String curUnit) {
        Currency currency = currencyRepository.findByCurUnit(curUnit)
                .orElseThrow(() -> new IllegalArgumentException("해당 통화가 존재하지 않습니다."));
        return new CurrencyInfoResponseDto(curUnit, currency.getDealBasR());
    }

}
