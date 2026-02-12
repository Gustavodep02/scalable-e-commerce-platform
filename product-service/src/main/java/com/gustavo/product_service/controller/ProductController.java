package com.gustavo.product_service.controller;

import com.gustavo.product_service.dto.CreateProductDto;
import com.gustavo.product_service.dto.ProductDetailsResponseDto;
import com.gustavo.product_service.dto.UpdateProductDto;
import com.gustavo.product_service.model.Product;
import com.gustavo.product_service.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name= "products", description = "Endpoints for managing products")
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @Operation(summary = "Creates a new product", description = "Creates a new product with the provided details")
    @ApiResponse(responseCode = "200", description = "Successfully created product")
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
    @Operation(summary = "Retrieves all products", description = "Returns a list of all products in the system")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products")
    public ResponseEntity<List<Product>> getAllProducts() {
        log.info("Received request to get all products");
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieves a product by ID", description = "Returns the product with the specified ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved product")
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
    @Operation(summary = "Updates a product", description = "Updates the details of an existing product")
    @ApiResponse(responseCode = "200", description = "Successfully updated product")
    public ResponseEntity<Product> updateProduct(@PathVariable UUID id, @RequestBody @Valid UpdateProductDto updateProductDTO) {
        log.info("Received request to update product with id: {}", id);
        return ResponseEntity.ok(productService.updateProduct(id, updateProductDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletes a product by ID", description = "Deletes the product with the specified ID from the system")
    @ApiResponse(responseCode = "204", description = "Product successfully deleted")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        log.info("Received request to delete product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
