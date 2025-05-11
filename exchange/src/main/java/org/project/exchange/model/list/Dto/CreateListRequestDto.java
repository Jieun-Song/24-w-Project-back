package org.project.exchange.model.list.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class CreateListRequestDto {
    private Long userId;  // 사용자 ID
    private Long currencyIdFrom; // 통화 ID
    private Long currencyIdTo; // 통화 ID
    private String location; // 위치

//    public CreateListRequestDto(Lists lists) {
//        this.userId = lists.getUser().getUserId();
//        this.currencyIdFrom = lists.getCurrencyFrom().getCurrencyId();
//        this.currencyIdTo = lists.getCurrencyTo().getCurrencyId();
//        this.location = lists.getLocation();
//    }
}
