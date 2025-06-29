package com.parcial.msorders.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que representa una orden de compra en el sistema.
 * Implementa el patrón Builder mediante la anotación @Builder de Lombok.
 */
@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    /**
     * Identificador único de la orden
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del cliente que realizó la orden
     */
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String customer;

    /**
     * Fecha y hora de creación de la orden
     */
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime date;

    /**
     * Estado actual de la orden (PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED)
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    /**
     * Lista de productos incluidos en la orden
     * Se establece una relación uno a muchos con cascade para mantener la integridad
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    /**
     * Monto total de la orden
     */
    @NotNull(message = "El total es obligatorio")
    @Positive(message = "El total debe ser mayor que cero")
    private BigDecimal total;
} 