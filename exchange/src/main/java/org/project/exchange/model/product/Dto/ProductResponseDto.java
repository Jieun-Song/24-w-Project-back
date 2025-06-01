package org.project.exchange.model.product.Dto;

import lombok.Getter;
import org.project.exchange.model.product.Product;

import java.time.LocalDateTime;

@Getter
public class ProductResponseDto {
    private Long productId;
    private String name;
    private Double originPrice;
    private Integer quantity;
    private Long listId;
    private Boolean deletedYn;
    private LocalDateTime createdAt;

    public ProductResponseDto(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.quantity = product.getQuantity();
        this.originPrice = product.getOriginPrice();
        this.listId = product.getLists().getListId();
        this.deletedYn = product.getDeletedYn();
        this.createdAt = product.getCreatedAt();
    }
}
