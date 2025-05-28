package org.project.exchange.model.product.Dto;

import lombok.Getter;

@Getter
public class ProductUpdateRequestDto {
    private Long productId;
    private String name;
    private Integer quantity;
    private Double originPrice;

    public ProductUpdateRequestDto(Long productId, String name, Integer quantity, Double originPrice) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.originPrice = originPrice;
    }
}
