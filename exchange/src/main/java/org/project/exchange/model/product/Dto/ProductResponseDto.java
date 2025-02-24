package org.project.exchange.model.product.Dto;

import lombok.Getter;
import org.project.exchange.model.product.Product;

@Getter
public class ProductResponseDto {
    private Long productId;
    private String name;
    private Double originPrice;
    private Long listId;

    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.originPrice = product.getOriginPrice();
        this.listId = product.getLists().getListId();
    }
}
