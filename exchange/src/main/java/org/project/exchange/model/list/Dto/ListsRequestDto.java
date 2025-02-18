package org.project.exchange.model.list.Dto;

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
    private String name;
    private Long userId;  // 사용자 ID
    private Long currencyId; // 통화 ID
    private String location; // 위치

    public Lists toEntity(String name, User user, Currency currency, LocalDateTime now) {
        return Lists.builder()
                .name(name)
                .location(this.location)
                .currency(currency)
                .user(user)
                .build();
    }

    public void setName(String name) {
        this.name = "name";
    }
}
