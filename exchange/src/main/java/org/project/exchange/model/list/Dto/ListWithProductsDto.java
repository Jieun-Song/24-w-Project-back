package org.project.exchange.model.list.Dto;

import lombok.Getter;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.product.Dto.ProductResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ListWithProductsDto {
    private Long listId;
    private String name;
    private String location;
    private String createdAt;
    private List<ProductResponseDto> products;

    public ListWithProductsDto(Lists lists) {
        this.listId = lists.getListId();
        this.name = lists.getName();
        this.location = lists.getLocation();
        this.createdAt = lists.getCreatedAt().toString();

        this.products = lists.getProducts().stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }
}
