package org.project.exchange.model.list.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Slf4j
public class CreateListResponseDto {
    private Long listId;
    private String name;
    private Long userId;
    private LocalDateTime now;
    private Long currencyFrom;
    private Long currencyTo;

    public CreateListResponseDto(Lists lists) {
        this.listId = lists.getListId();
        this.name = lists.getName();
        this.userId = lists.getUser().getUserId();
        this.now = lists.getCreatedAt();
        this.currencyFrom = lists.getCurrencyFrom().getCurrencyId();
        this.currencyTo = lists.getCurrencyTo().getCurrencyId();
    }
}
