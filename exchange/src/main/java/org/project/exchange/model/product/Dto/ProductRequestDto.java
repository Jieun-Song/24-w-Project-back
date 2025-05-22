package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private Double originPrice;
    private Long listId;

    public ProductRequestDto(String name, Double originPrice, Long listId) {
        this.name = name;
        this.originPrice = originPrice;
        this.listId = listId;
    }
}
