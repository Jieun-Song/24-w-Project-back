package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.product.Product;

@Getter
@NoArgsConstructor
public class ProductRequestDto {
    private String name;
    private Double originPrice;
    private Long listId;

    public ProductRequestDto(String name, Double originPrice, Long listId) {
        this.name = name;
        this.originPrice = originPrice;
        this.listId = listId;
    }

//    public Product toEntity(String productName, Lists lists) {
//        return Product.builder()
//                .name(productName)
//                .originPrice(this.originPrice)
//                .lists(lists)
//                .build();
//    }
}
