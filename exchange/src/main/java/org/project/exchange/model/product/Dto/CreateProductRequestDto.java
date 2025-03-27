package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductRequestDto {
    private Long listId;
    private String name;
    private Double originPrice;

    public CreateProductRequestDto(Long listId, String name, Double originPrice) {
        this.listId = listId;
        this.name = name;
        this.originPrice = originPrice;
    }
}
