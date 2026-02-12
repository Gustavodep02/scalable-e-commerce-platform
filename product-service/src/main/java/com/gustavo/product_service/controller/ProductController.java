package com.gustavo.product_service.controller;

import com.gustavo.product_service.dto.CreateProductDto;
import com.gustavo.product_service.dto.ProductDetailsResponseDto;
import com.gustavo.product_service.dto.UpdateProductDto;
import com.gustavo.product_service.model.Product;
import com.gustavo.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid CreateProductDto createProductDTO) {
        log.info("Received request to create product with name: {}", createProductDTO.name());
        var product = new Product();
        product.setName(createProductDTO.name());
        product.setPrice(createProductDTO.price());
        product.setStock(createProductDTO.stock());
        product.setDescription(createProductDTO.description());
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Received request to get all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetailsResponseDto> getProductById(@PathVariable UUID id) {
        log.info("Received request to get product with id: {}", id);
        Product product = productService.getProductById(id);

        return ResponseEntity.ok(
                new ProductDetailsResponseDto(
                        product.getId(),
                        product.getName(),
                        product.getDescription(),
                        product.getPrice(),
                        product.getStock()
                )
        );
    }


    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody @Valid UpdateProductDto updateProductDTO) {
        log.info("Received request to update product with id: {}", id);
        return ResponseEntity.ok(productService.updateProduct(id, updateProductDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.info("Received request to delete product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
