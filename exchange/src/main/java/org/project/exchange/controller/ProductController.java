package org.project.exchange.controller;

import lombok.RequiredArgsConstructor;
import org.project.exchange.model.product.Dto.ProductRequestDto;
import org.project.exchange.model.product.Dto.ProductResponseDto;
import org.project.exchange.global.api.ApiResponse;
import org.project.exchange.model.product.Product;
import org.project.exchange.model.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody ProductRequestDto requestDto) {
        Product newProduct = productService.save(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.createSuccessWithMessage(newProduct, "상품 등록 성공"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getProductByListsId(@PathVariable Long id) {
        List<ProductResponseDto> product = productService.findByListsId(id);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(product, "상품 조회 성공"));}

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(products, "상품 조회 성공"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto requestDto) {
        Product updatedProduct = productService.update(id, requestDto);
        return ResponseEntity.ok(ApiResponse.createSuccessWithMessage(updatedProduct, "상품 수정 성공"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.createSuccessWithMessage(null, "상품 삭제 성공"));}
}

