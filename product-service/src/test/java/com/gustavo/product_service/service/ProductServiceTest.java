package com.gustavo.product_service.service;

import com.gustavo.product_service.dto.UpdateProductDto;
import com.gustavo.product_service.model.Product;
import com.gustavo.product_service.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("Should return all products when repository is not empty")
    void getAllProductsReturnsAllProducts() {
        Mockito.when(productRepository.findAll()).thenReturn(List.of(
                new Product(UUID.randomUUID(), "Product 1", "Description 1", 10.0, 100),
                new Product(UUID.randomUUID(), "Product 2", "Description 2", 20.0, 200)
        ));
        List<Product> products = productService.getAllProducts();
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals("Product 1", products.get(0).getName());
        assertEquals("Product 2", products.get(1).getName());
    }
    @Test
    @DisplayName("Should save and return the product")
    void saveProductReturnsSavedProduct() {
        var product = new Product(UUID.randomUUID(), "Product 1", "Description 1", 10.0, 100);
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product savedProduct = productService.createProduct(product);
        assertNotNull(savedProduct);
        assertEquals("Product 1", savedProduct.getName());
        assertEquals("Description 1", savedProduct.getDescription());
        assertEquals(10.0, savedProduct.getPrice());
        assertEquals(100, savedProduct.getStock());
    }

    @Test
    @DisplayName("Should return product by ID when it exists")
    void getProductByIdReturnsProduct() {
        var id = UUID.randomUUID();
        var product = new Product(id, "Product 1", "Description 1", 10.0, 100);
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Product foundProduct = productService.getProductById(id);
        assertNotNull(foundProduct);
        assertEquals("Product 1", foundProduct.getName());
        assertEquals("Description 1", foundProduct.getDescription());
        assertEquals(10.0, foundProduct.getPrice());
        assertEquals(100, foundProduct.getStock());
    }
    @Test
    @DisplayName("Should throw RuntimeException when product ID does not exist")
    void getProductByIdReturnsNullIfNotExists() {
        var id = UUID.randomUUID();
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        var exception = assertThrows(RuntimeException.class, () ->
                productService.getProductById(id)
        );

        assertEquals("Product not found", exception.getMessage());

    }
    @Test
    @DisplayName("Should update and return the patched product")
    void patchProductUpdatesAndReturnsProduct() {
        var id = UUID.randomUUID();
        var product = new Product(id, "Product 1", "Description 1", 10.0, 100);
        var updateProductDto = new UpdateProductDto(
                "Updated Product",
                "Updated Description",
                20.0,
                200
        );
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.when(productRepository.save(product)).thenReturn(product);
        Product updatedProduct = productService.updateProduct(id, updateProductDto);
        assertNotNull(updatedProduct);
        assertEquals("Updated Product", updatedProduct.getName());
        assertEquals("Updated Description", updatedProduct.getDescription());
        assertEquals(20.0, updatedProduct.getPrice());
        assertEquals(200, updatedProduct.getStock());
    }
    @Test
    @DisplayName("Should return null when patching a non-existent product")
    void patchProductReturnsNullIfProductNotExists() {
        var id = UUID.randomUUID();
        var updateProductDto = new UpdateProductDto(
                "Updated Product",
                "Updated Description",
                20.0,
                200
        );
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.empty());
        var exception = assertThrows(RuntimeException.class, () ->
                productService.updateProduct(id, updateProductDto)
        );

        assertEquals("Product not found", exception.getMessage());

    }
    @Test
    @DisplayName("Should delete product by ID")
    void deleteProductDeletesProduct() {
        var id = UUID.randomUUID();
        var product = new Product(id, "Product 1", "Description 1", 10.0, 100);
        Mockito.when(productRepository.findById(id)).thenReturn(Optional.of(product));
        Mockito.doNothing().when(productRepository).delete(product);
        productService.deleteProduct(id);
        Mockito.verify(productRepository, Mockito.times(1)).delete(product);
    }
}
