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
public class CreateRequest {
    private Long userId;  // 사용자 ID
    private Long currencyId; // 통화 ID
    private LocalDateTime now;
    private String location; // 위치

    public CreateRequest(Lists lists) {
        this.userId = lists.getUser().getUserId();
        this.currencyId = lists.getCurrency().getCurrencyId();
        this.now = lists.getCreatedAt();
        this.location = lists.getLocation();
    }
}
