package org.project.exchange.model.product.Dto;

import lombok.Getter;

@Getter
public class ProductUpdateRequestDto {
    private Long productId;
    private String name;
    private Double originPrice;

    public ProductUpdateRequestDto(Long productId, String name, Double originPrice) {
        this.productId = productId;
        this.name = name;
        this.originPrice = originPrice;
    }
}
