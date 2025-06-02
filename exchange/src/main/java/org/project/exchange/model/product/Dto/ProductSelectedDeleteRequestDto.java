package org.project.exchange.model.product.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSelectedDeleteRequestDto {
    @JsonProperty("product_ids")
    private List<Long> productIds;

}
