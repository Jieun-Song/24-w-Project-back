package org.project.exchange.model.list.Dto;

import lombok.Getter;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;

import java.time.LocalDateTime;

@Getter
public class ListsResponseDto {
    private Long listId;
    private String name;
    private Long userId;
    private String location;
    private LocalDateTime createdAt;
    private Long currencyFromId;
    private Long currencyToId;
    private Boolean deletedYn;

    public ListsResponseDto(Lists lists) {
        this.listId = lists.getListId();
        this.name = lists.getName();
        this.userId = lists.getUser().getUserId();
        this.createdAt = lists.getCreatedAt();
        this.location = lists.getLocation();
        this.currencyFromId = lists.getCurrencyFrom().getCurrencyId();
        this.currencyToId = lists.getCurrencyTo().getCurrencyId();
        this.deletedYn = lists.getDeletedYn();
    }
}
