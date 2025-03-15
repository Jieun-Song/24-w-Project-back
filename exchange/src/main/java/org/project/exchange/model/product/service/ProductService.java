package org.project.exchange.model.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project.exchange.model.currency.repository.CurrencyRepository;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.repository.ListsRepository;
import org.project.exchange.model.product.Dto.*;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.product.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

    public Product save(ProductRequestDto requestDto) {
        Lists lists = listsRepository.findById(requestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        long productCount = productRepository.countAllProduct()+1;
        String productName = "상품" + productCount;
        Product product =new Product(productName, requestDto.getOriginPrice(), lists);
        productRepository.save(product);
        return product;
    }

    public Product update(Long productId, ProductRequestDto requestDto) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        product.setName(requestDto.getName());
        product.setOriginPrice(requestDto.getOriginPrice());
        Lists lists = listsRepository.findById(requestDto.getListId())
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 존재하지 않습니다."));
        product.setLists(lists);
        productRepository.save(product);
        /***
         * 이거 이렇게 save해도 되는지,, update 다시
         * update repository에 update 메서드 만들어야할듯
         */
        return product;
    }


    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }

    public void deleteByIds(List<Long> ids) {
        for (Long id : ids) {
            productRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        }
        productRepository.deleteByIds(ids);
    }

    public void deleteByListId(Long listId) {
        productRepository.deleteByListId(listId);
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
