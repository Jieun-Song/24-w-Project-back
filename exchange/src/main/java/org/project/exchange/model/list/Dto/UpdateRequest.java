package org.project.exchange.model.list.Dto;

import lombok.Getter;

@Getter
public class UpdateRequest {
    private Long currencyId; // 통화 ID
    private String location; // 위치
    private String name; // 리스트 이름
}
