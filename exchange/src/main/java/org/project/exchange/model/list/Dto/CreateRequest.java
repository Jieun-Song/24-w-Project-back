package org.project.exchange.model.list.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.list.Lists;


@Getter
@NoArgsConstructor
@Slf4j
public class CreateRequest {
    private Long userId;  // 사용자 ID
    private Long currencyIdFrom; // 통화 ID
    private Long currencyIdTo; // 통화 ID
    private String location; // 위치

    public CreateRequest(Lists lists) {
        this.userId = lists.getUser().getUserId();
        this.currencyIdFrom = lists.getCurrencyFrom().getCurrencyId();
        this.currencyIdTo = lists.getCurrencyTo().getCurrencyId();
        this.location = lists.getLocation();
    }
}
