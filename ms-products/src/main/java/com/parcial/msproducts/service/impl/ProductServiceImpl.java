package com.parcial.msproducts.service.impl;

import com.parcial.msproducts.dto.ProductDTO;
import com.parcial.msproducts.exception.ResourceNotFoundException;
import com.parcial.msproducts.model.Product;
import com.parcial.msproducts.repository.ProductRepository;
import com.parcial.msproducts.service.ProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del servicio de productos.
 * Contiene la lógica de negocio para gestionar productos.
 * Implementa el patrón Circuit Breaker para la verificación de stock.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    /**
     * Crea un nuevo producto en el sistema
     * @param productDTO Datos del producto a crear
     * @return El producto creado con su ID asignado
     */
    @Override
    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = mapToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return mapToDTO(savedProduct);
    }

    /**
     * Obtiene un producto por su ID
     * @param id ID del producto a buscar
     * @return El producto encontrado
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Override
    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        return mapToDTO(product);
    }

    /**
     * Obtiene todos los productos con paginación
     * @param pageable Información de paginación
     * @return Página de productos
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToDTO);
    }

    /**
     * Busca productos por nombre (búsqueda parcial)
     * @param name Nombre o parte del nombre a buscar
     * @param pageable Información de paginación
     * @return Página de productos que coinciden con la búsqueda
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> searchProductsByName(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name, pageable).map(this::mapToDTO);
    }

    /**
     * Filtra productos por categoría
     * @param category Categoría a filtrar
     * @param pageable Información de paginación
     * @return Página de productos de la categoría especificada
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsByCategory(String category, Pageable pageable) {
        return productRepository.findByCategoryIgnoreCase(category, pageable).map(this::mapToDTO);
    }

    /**
     * Actualiza un producto existente
     * @param id ID del producto a actualizar
     * @param productDTO Nuevos datos del producto
     * @return El producto actualizado
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
        
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setCategory(productDTO.getCategory());
        
        Product updatedProduct = productRepository.save(product);
        return mapToDTO(updatedProduct);
    }

    /**
     * Elimina un producto por su ID
     * @param id ID del producto a eliminar
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productRepository.deleteById(id);
    }

    /**
     * Verifica si hay suficiente stock para un producto
     * Implementa el patrón Circuit Breaker para simular una llamada externa
     * @param productId ID del producto a verificar
     * @param quantity Cantidad requerida
     * @return true si hay suficiente stock, false en caso contrario
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Override
    @CircuitBreaker(name = "stockService", fallbackMethod = "checkStockFallback")
    public boolean checkStock(Long productId, Integer quantity) {
        // Simulando una llamada a un servicio externo para verificar stock
        log.info("Verificando stock para el producto {} con cantidad {}", productId, quantity);
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + productId));
        
        // Simular un retraso para probar el circuit breaker
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return product.getStock() >= quantity;
    }
    
    /**
     * Método de respaldo (fallback) para el circuit breaker
     * Se ejecuta cuando el método checkStock falla o tarda demasiado
     * @param productId ID del producto
     * @param quantity Cantidad requerida
     * @param e Excepción que causó el fallo
     * @return false por defecto cuando el servicio no está disponible
     */
    public boolean checkStockFallback(Long productId, Integer quantity, Exception e) {
        log.error("Circuit breaker activado para el producto {}: {}", productId, e.getMessage());
        // Por defecto retorna false cuando el servicio no está disponible
        return false;
    }
    
    /**
     * Convierte un DTO a una entidad Product
     * Utiliza el patrón Builder
     */
    private Product mapToEntity(ProductDTO productDTO) {
        return Product.builder()
                .id(productDTO.getId())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .stock(productDTO.getStock())
                .category(productDTO.getCategory())
                .build();
    }
    
    /**
     * Convierte una entidad Product a un DTO
     * Utiliza el patrón Builder
     */
    private ProductDTO mapToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .build();
    }
} 