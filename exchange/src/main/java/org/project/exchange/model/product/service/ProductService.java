package org.project.exchange.model.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.repository.ListsRepository;
import org.project.exchange.model.product.Dto.*;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ListsRepository listsRepository;

    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate restTemplate;

    public List<ProductResponseDto> findByListsId(Long listId) {
        return productRepository.findByListId(listId)
                .stream()
                .map(ProductResponseDto::new)
                .collect(Collectors.toList());
    }

    public CreateProductResponseDto save(CreateProductRequestDto requestDto) {
        Lists lists = listsRepository.findById(requestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));

        long productCount = productRepository.countAllProductByListId(requestDto.getListId())+1;

        String productName;

        if (requestDto.getName() == null || requestDto.getName() == "") {
            productName = "상품" + productCount;
        }else {
            productName = requestDto.getName();
        }
        LocalDateTime createdAt = LocalDateTime.now();

        Product product = new Product(productName, createdAt, requestDto.getOriginPrice(), lists);
        productRepository.save(product);

        return new CreateProductResponseDto(product);
    }

    public ProductResponseDto update(ProductUpdateRequestDto requestDto) {
        Product product = productRepository.findById(requestDto.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        product.updateNameAndPrice(requestDto.getName(), requestDto.getOriginPrice());
        productRepository.save(product);
        return new ProductResponseDto(product);
    }


    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }

    public void deleteByIds(List<Long> productIds) {
        if (productIds == null || productIds.isEmpty()) {
            throw new IllegalArgumentException("삭제할 상품 ID 리스트가 없습니다.");
        }

        for (Long id : productIds) {
            Product product = productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
            productRepository.delete(product);
        }
    }

//    public void deleteByListId(Long listId) {
//        productRepository.deleteByListId(listId);
//    }

    public List<ProductWithCurrencyDto> findAll(Long userId) {
        List<Object[]> result =  productRepository.findAllByUser(userId);

        List<ProductWithCurrencyDto> dtoList = result.stream()
                .map(r -> new ProductWithCurrencyDto((Product) r[0], (Long) r[1]))
                .toList();
        return dtoList;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    // 이미지에서 상품명과 가격만 추출
    public List<ImageProductResponseDto> analyzeImage(String base64Image) {
        String responseContent = null;
        ChatGPTRequest request = new ChatGPTRequest(model, base64Image);
        ChatGPTResponse chatGPTResponse = restTemplate.postForObject(apiURL, request, ChatGPTResponse.class);
        responseContent = chatGPTResponse.getChoices().get(0).getMessage().getContent().toString();
        return parseProductList(responseContent);
    }

    public static List<ImageProductResponseDto> parseProductList(String input) {
        List<ImageProductResponseDto> productList = new ArrayList<>();

        // 줄바꿈을 기준으로 문자열을 분리 (연속된 개행 문자를 제거하고 분리)
        String[] lines = input.trim().split("\\n+");

        for (String line : lines) {
            if (!line.trim().isEmpty()) {
                String[] parts = line.split(" - ");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    String price = parts[1].trim().replaceAll("[^0-9]", "");
                    Double priceDouble = Double.valueOf(price);
                    productList.add(new ImageProductResponseDto(name, priceDouble));
                }
            }
        }

        return productList;
    }
}
