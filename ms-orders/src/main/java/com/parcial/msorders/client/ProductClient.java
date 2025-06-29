package com.parcial.msorders.client;

import com.parcial.msorders.dto.ProductDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-products", url = "${products.service.url}")
public interface ProductClient {

    @GetMapping("/api/products/{id}")
    ResponseEntity<ProductDTO> getProductById(@PathVariable("id") Long id);
    
    @GetMapping("/api/products/{id}/check-stock")
    ResponseEntity<Boolean> checkStock(
            @PathVariable("id") Long id,
            @RequestParam("quantity") Integer quantity);
} 