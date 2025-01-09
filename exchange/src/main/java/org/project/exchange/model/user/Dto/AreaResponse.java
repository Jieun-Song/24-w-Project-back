package org.project.exchange.model.user.Dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AreaResponse {

    private Long code;
    private String name;

    public AreaResponse(Long code, String name) {
        this.code = code;
        this.name = name;
    }
}