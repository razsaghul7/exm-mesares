package com.parcial.msproducts.controller;

import com.parcial.msproducts.dto.ProductDTO;
import com.parcial.msproducts.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las operaciones de productos.
 * Expone los endpoints para crear, leer, actualizar y eliminar productos.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * Crea un nuevo producto
     * @param productDTO Datos del producto a crear (validados)
     * @return El producto creado con código de estado 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductDTO createdProduct = productService.createProduct(productDTO);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Obtiene un producto por su ID
     * @param id ID del producto a buscar
     * @return El producto encontrado con código de estado 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = productService.getProductById(id);
        return ResponseEntity.ok(productDTO);
    }

    /**
     * Obtiene todos los productos con paginación
     * @param pageable Información de paginación (tamaño, página, ordenamiento)
     * @return Página de productos con código de estado 200 (OK)
     */
    @GetMapping
    public ResponseEntity<Page<ProductDTO>> getAllProducts(
            @PageableDefault(size = 10, sort = "name") Pageable pageable) {
        Page<ProductDTO> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    /**
     * Busca productos por nombre o categoría
     * @param name Nombre o parte del nombre (opcional)
     * @param category Categoría (opcional)
     * @param pageable Información de paginación
     * @return Página de productos que coinciden con los criterios de búsqueda
     */
    @GetMapping("/search")
    public ResponseEntity<Page<ProductDTO>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<ProductDTO> products;
        
        if (name != null && !name.isEmpty()) {
            products = productService.searchProductsByName(name, pageable);
        } else if (category != null && !category.isEmpty()) {
            products = productService.getProductsByCategory(category, pageable);
        } else {
            products = productService.getAllProducts(pageable);
        }
        
        return ResponseEntity.ok(products);
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productDTO Nuevos datos del producto (validados)
     * @return El producto actualizado con código de estado 200 (OK)
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDTO productDTO) {
        ProductDTO updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     * @return Respuesta vacía con código de estado 204 (NO_CONTENT)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Verifica si hay suficiente stock para un producto
     * @param id ID del producto a verificar
     * @param quantity Cantidad requerida
     * @return true si hay suficiente stock, false en caso contrario
     */
    @GetMapping("/{id}/check-stock")
    public ResponseEntity<Boolean> checkStock(
            @PathVariable Long id,
            @RequestParam Integer quantity) {
        boolean hasStock = productService.checkStock(id, quantity);
        return ResponseEntity.ok(hasStock);
    }
} 