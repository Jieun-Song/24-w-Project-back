package org.project.exchange.model.product.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductSelectedDeleteRequestDto {
    @JsonProperty("product_ids")
    private List<Long> productIds;

    public ProductSelectedDeleteRequestDto(List<Long> productIds) {
        this.productIds = productIds;
    }
}
