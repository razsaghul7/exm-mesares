package com.parcial.msorders.controller;

import com.parcial.msorders.dto.OrderDTO;
import com.parcial.msorders.model.OrderStatus;
import com.parcial.msorders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para gestionar las operaciones de órdenes.
 * Expone los endpoints para crear órdenes y consultar su estado.
 */
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * Crea una nueva orden
     * @param orderDTO Datos de la orden a crear (validados)
     * @return La orden creada con código de estado 201 (CREATED)
     */
    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }

    /**
     * Obtiene una orden por su ID
     * @param id ID de la orden a buscar
     * @return La orden encontrada con código de estado 200 (OK)
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        OrderDTO orderDTO = orderService.getOrderById(id);
        return ResponseEntity.ok(orderDTO);
    }

    /**
     * Obtiene todas las órdenes con paginación
     * @param pageable Información de paginación (tamaño, página, ordenamiento)
     * @return Página de órdenes con código de estado 200 (OK)
     */
    @GetMapping
    public ResponseEntity<Page<OrderDTO>> getAllOrders(
            @PageableDefault(size = 10, sort = "date") Pageable pageable) {
        Page<OrderDTO> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }

    /**
     * Busca órdenes por cliente o estado
     * @param customer Nombre o parte del nombre del cliente (opcional)
     * @param status Estado de la orden (opcional)
     * @param pageable Información de paginación
     * @return Página de órdenes que coinciden con los criterios de búsqueda
     */
    @GetMapping("/search")
    public ResponseEntity<Page<OrderDTO>> searchOrders(
            @RequestParam(required = false) String customer,
            @RequestParam(required = false) OrderStatus status,
            @PageableDefault(size = 10) Pageable pageable) {
        
        Page<OrderDTO> orders;
        
        if (customer != null && !customer.isEmpty() && status != null) {
            orders = orderService.getOrdersByCustomerAndStatus(customer, status, pageable);
        } else if (customer != null && !customer.isEmpty()) {
            orders = orderService.getOrdersByCustomer(customer, pageable);
        } else if (status != null) {
            orders = orderService.getOrdersByStatus(status, pageable);
        } else {
            orders = orderService.getAllOrders(pageable);
        }
        
        return ResponseEntity.ok(orders);
    }

    /**
     * Actualiza el estado de una orden existente
     * @param id ID de la orden a actualizar
     * @param status Nuevo estado
     * @return La orden actualizada con código de estado 200 (OK)
     */
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) {
        OrderDTO updatedOrder = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(updatedOrder);
    }
} 