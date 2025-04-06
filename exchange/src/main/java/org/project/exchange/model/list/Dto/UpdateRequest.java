package org.project.exchange.model.list.Dto;

import lombok.Getter;

@Getter
public class UpdateRequest {
    private Long listId;
    private Long currencyIdFrom; // 통화 ID
    private Long currencyIdTo; // 통화 ID
    private String location; // 위치
    private String name; // 리스트 이름
}
