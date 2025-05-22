package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.Setter;
import org.project.exchange.model.product.Product;

@Getter
@Setter
public class ProductWithCurrencyDto {
    private final Long productId;
    private final Long listId;
    private final String name;
    private final Double originPrice;
    private final Long currencyId;
    private final String createdAt;
    private final Boolean deletedYn;

    public ProductWithCurrencyDto(Product product, Long currencyId) {
        this.productId = product.getProductId();
        this.listId = product.getLists().getListId();
        this.name = product.getName();
        this.originPrice = product.getOriginPrice();
        this.deletedYn = product.getDeletedYn();
        this.createdAt = product.getCreatedAt().toString();
        this.currencyId = currencyId;

    }
}
