package org.project.exchange.model.list.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
@Slf4j
public class CreateListResponseDto {
    private Long listId;
    private String name;
    private Long userId;
    private String createdAt;
    private Currency currencyFrom;
    private Currency currencyTo;
    private String location;

    public CreateListResponseDto(Lists lists) {
        this.listId = lists.getListId();
        this.name = lists.getName();
        this.userId = lists.getUser().getUserId();
        this.location = lists.getLocation();
        this.createdAt = lists.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
        this.currencyFrom = lists.getCurrencyFrom();
        this.currencyTo = lists.getCurrencyTo();
    }
}
