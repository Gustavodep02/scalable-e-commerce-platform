package com.gustavo.product_service.service;

import com.gustavo.product_service.dto.UpdateProductDto;
import com.gustavo.product_service.model.Product;
import com.gustavo.product_service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product createProduct(Product product) {
        productRepository.save(product);
        return product;
    }
    public Product updateProduct(UUID id, UpdateProductDto updateProductDto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setName(updateProductDto.name());
        product.setPrice(updateProductDto.price());
        product.setStock(updateProductDto.stock());
        product.setDescription(updateProductDto.description());

        productRepository.save(product);
        return product;
    }
    public void deleteProduct(UUID id) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }
    public Product getProductById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    public java.util.List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
