package com.gustavo.product_service.controller;

import com.gustavo.product_service.dto.CreateProductDto;
import com.gustavo.product_service.dto.UpdateProductDto;
import com.gustavo.product_service.model.Product;
import com.gustavo.product_service.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody @Valid CreateProductDto createProductDTO) {
        var product = new Product();
        product.setName(createProductDTO.name());
        product.setPrice(createProductDTO.price());
        product.setStock(createProductDTO.stock());
        product.setDescription(createProductDTO.description());
        return ResponseEntity.ok(productService.createProduct(product));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody @Valid UpdateProductDto updateProductDTO) {

        return ResponseEntity.ok(productService.updateProduct(id, updateProductDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
