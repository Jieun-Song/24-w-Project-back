package org.project.exchange.model.product.Dto;

import lombok.Getter;
import org.project.exchange.model.product.Product;

@Getter
public class CreateProductResponseDto {
    private Long productId;
    private String name;
    private Double originPrice;
    private Long listId;

    public CreateProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.originPrice = product.getOriginPrice();
        this.listId = product.getLists().getListId();
    }
}
