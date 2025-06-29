package com.parcial.msproducts.service;

import com.parcial.msproducts.dto.ProductDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    
    ProductDTO createProduct(ProductDTO productDTO);
    
    ProductDTO getProductById(Long id);
    
    Page<ProductDTO> getAllProducts(Pageable pageable);
    
    Page<ProductDTO> searchProductsByName(String name, Pageable pageable);
    
    Page<ProductDTO> getProductsByCategory(String category, Pageable pageable);
    
    ProductDTO updateProduct(Long id, ProductDTO productDTO);
    
    void deleteProduct(Long id);
    
    boolean checkStock(Long productId, Integer quantity);
} 