package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.exchange.model.currency.Currency;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.product.Product;

@Getter
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private Double originPrice;
    private Long listId;
    private Long currencyId;

    public ProductRequestDto(String name, Double originPrice, Long listId, Long currencyId) {
        this.name = name;
        this.originPrice = originPrice;
        this.listId = listId;
        this.currencyId = currencyId;
    }

    public Product toEntity(Lists lists, Currency currency) {
        double currencyRate = currency.getDealBasR();
        return Product.builder()
                .name(this.name)
                .originPrice(this.originPrice)
                .convertedPrice(this.originPrice/1000*currencyRate)
                .currencyRate(currencyRate)
                .lists(lists)
                .currency(currency)
                .build();
    }
}
