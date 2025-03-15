package org.project.exchange.model.product.Dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ProductSelectedDeleteRequestDto {
    private List<Long> productIds;

    public ProductSelectedDeleteRequestDto(List<Long> productIds) {
        this.productIds = productIds;
    }
}
