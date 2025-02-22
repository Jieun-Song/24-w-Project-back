package org.project.exchange.model.list.Dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.user.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Slf4j
public class ListsRequestDto {
    private Long userId;  // 사용자 ID
    private Long currencyId; // 통화 ID
    private LocalDateTime now;
    private String location; // 위치

    public ListsRequestDto(Lists lists) {
        this.userId = lists.getUser().getUserId();
        this.currencyId = lists.getCurrency().getCurrencyId();
        this.now = lists.getCreatedAt();
        this.location = lists.getLocation();
    }
}
