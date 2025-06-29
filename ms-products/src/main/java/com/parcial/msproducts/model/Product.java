package com.parcial.msproducts.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.math.BigDecimal;

/**
 * Entidad que representa un producto en el sistema.
 * Implementa el patrón Builder mediante la anotación @Builder de Lombok.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    /**
     * Identificador único del producto
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del producto
     */
    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    /**
     * Descripción detallada del producto
     */
    @Column(length = 1000)
    private String description;

    /**
     * Precio del producto
     */
    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que cero")
    private BigDecimal price;

    /**
     * Cantidad disponible en inventario
     */
    @NotNull(message = "El stock es obligatorio")
    @PositiveOrZero(message = "El stock debe ser cero o positivo")
    private Integer stock;

    /**
     * Categoría a la que pertenece el producto
     */
    @NotBlank(message = "La categoría es obligatoria")
    private String category;
} 