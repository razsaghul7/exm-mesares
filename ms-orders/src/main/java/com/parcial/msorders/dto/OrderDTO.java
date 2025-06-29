package com.parcial.msorders.dto;

import com.parcial.msorders.model.OrderStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para transferencia de datos de órdenes
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {
    
    private Long id;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String customer;
    
    private LocalDateTime date;
    
    private OrderStatus status;
    
    @NotEmpty(message = "La orden debe tener al menos un ítem")
    @Valid
    private List<OrderItemDTO> items;
    
    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor que cero")
    private BigDecimal total;
} 