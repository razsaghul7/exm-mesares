package com.parcial.msorders.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para transferencia de datos de ítems de órdenes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {
    
    private Long id;
    
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productId;
    
    private String productName;
    
    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor que cero")
    private Integer quantity;
    
    private BigDecimal unitPrice;
    
    private BigDecimal subtotal;
} 