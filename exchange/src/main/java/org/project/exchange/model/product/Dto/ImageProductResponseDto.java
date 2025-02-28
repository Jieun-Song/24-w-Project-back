package org.project.exchange.model.product.Dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ImageProductResponseDto {
    private String name;
    private Double price;

    public ImageProductResponseDto(String name, Double price) {
        this.name = name;
        this.price = price;
    }
}
