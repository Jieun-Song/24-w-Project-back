package org.project.exchange.model.list.Dto;

import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;

import java.time.LocalDateTime;

public class CreateResponse {
    private Long listId;
    private String name;
    private Long userId;
    private LocalDateTime now;
    private Currency currencyFrom;
    private Currency currencyTo;

    public CreateResponse(Lists lists) {
        this.listId = lists.getListId();
        this.name = lists.getName();
        this.userId = lists.getUser().getUserId();
        this.now = lists.getCreatedAt();
        this.currencyFrom = lists.getCurrencyFrom();
        this.currencyTo = lists.getCurrencyTo();
    }
}
