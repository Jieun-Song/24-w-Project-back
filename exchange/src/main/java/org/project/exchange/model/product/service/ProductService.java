package org.project.exchange.model.product.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.project.exchange.model.currency.repository.CurrencyRepository;
import org.project.exchange.model.list.Lists;
import org.project.exchange.model.list.repository.ListsRepository;
import org.project.exchange.model.product.Dto.ProductRequestDto;
import org.project.exchange.model.product.Dto.ProductResponseDto;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.product.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ListsRepository listsRepository;

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
        Product product = requestDto.toEntity(productName,lists);
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
        return product;
    }


    public void delete(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품이 존재하지 않습니다."));
        productRepository.delete(product);
    }

    public void deleteByListId(Long listId) {
        productRepository.deleteByListId(listId);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }
}
