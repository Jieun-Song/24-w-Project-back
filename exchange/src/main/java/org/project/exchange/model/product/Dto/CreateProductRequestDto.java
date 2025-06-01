package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateProductRequestDto {
    private Long listId;
    private String name;
    private Integer quantity;
    private Double originPrice;

    public CreateProductRequestDto(Long listId, String name, Integer quantity, Double originPrice) {
        this.listId = listId;
        this.name = name;
        this.quantity = quantity;
        this.originPrice = originPrice;
    }
}
