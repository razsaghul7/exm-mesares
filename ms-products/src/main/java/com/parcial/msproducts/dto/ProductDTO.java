package com.parcial.msproducts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferencia de datos de productos
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;
    
    private String description;
    
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal price;
    
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock debe ser cero o positivo")
    private Integer stock;
    
    @NotBlank(message = "La categoría es obligatoria")
    private String category;
} 