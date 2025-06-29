package com.parcial.msorders.service.impl;

import com.parcial.msorders.client.ProductClient;
import com.parcial.msorders.dto.OrderDTO;
import com.parcial.msorders.dto.OrderItemDTO;
import com.parcial.msorders.dto.ProductDTO;
import com.parcial.msorders.exception.InsufficientStockException;
import com.parcial.msorders.exception.ResourceNotFoundException;
import com.parcial.msorders.model.Order;
import com.parcial.msorders.model.OrderItem;
import com.parcial.msorders.model.OrderStatus;
import com.parcial.msorders.repository.OrderRepository;
import com.parcial.msorders.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de órdenes.
 * Contiene la lógica de negocio para gestionar órdenes de compra.
 * Implementa el patrón Circuit Breaker para la comunicación con el servicio de productos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductClient productClient;

    /**
     * Crea una nueva orden en el sistema
     * Implementa el patrón Circuit Breaker para manejar fallos en la comunicación
     * con el servicio de productos
     * @param orderDTO Datos de la orden a crear
     * @return La orden creada con su ID asignado
     * @throws InsufficientStockException si no hay suficiente stock para algún producto
     * @throws ResourceNotFoundException si algún producto no existe
     */
    @Override
    @Transactional
    @CircuitBreaker(name = "productService", fallbackMethod = "createOrderFallback")
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Validar stock para todos los productos en la orden
        for (OrderItemDTO itemDTO : orderDTO.getItems()) {
            ResponseEntity<Boolean> stockResponse = productClient.checkStock(
                    itemDTO.getProductId(), itemDTO.getQuantity());
            
            if (!Boolean.TRUE.equals(stockResponse.getBody())) {
                throw new InsufficientStockException("Stock insuficiente para el producto con ID: " + itemDTO.getProductId());
            }
            
            // Obtener detalles del producto
            ResponseEntity<ProductDTO> productResponse = productClient.getProductById(itemDTO.getProductId());
            ProductDTO product = productResponse.getBody();
            
            if (product == null) {
                throw new ResourceNotFoundException("Producto no encontrado con id: " + itemDTO.getProductId());
            }
            
            // Establecer nombre del producto y precios
            itemDTO.setProductName(product.getName());
            itemDTO.setUnitPrice(product.getPrice());
            itemDTO.setSubtotal(product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
        }
        
        // Calcular total
        BigDecimal total = orderDTO.getItems().stream()
                .map(OrderItemDTO::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        orderDTO.setTotal(total);
        orderDTO.setDate(LocalDateTime.now());
        orderDTO.setStatus(OrderStatus.PENDING);
        
        // Convertir DTO a entidad
        Order order = mapToEntity(orderDTO);
        Order savedOrder = orderRepository.save(order);
        
        return mapToDTO(savedOrder);
    }
    
    /**
     * Método de respaldo (fallback) para el circuit breaker
     * Se ejecuta cuando el método createOrder falla o tarda demasiado
     * @param orderDTO Datos de la orden
     * @param e Excepción que causó el fallo
     * @return No retorna valor, lanza una excepción
     */
    public OrderDTO createOrderFallback(OrderDTO orderDTO, Exception e) {
        log.error("Circuit breaker activado para crear orden: {}", e.getMessage());
        throw new RuntimeException("El servicio de productos no está disponible, intente más tarde", e);
    }

    /**
     * Obtiene una orden por su ID
     * @param id ID de la orden a buscar
     * @return La orden encontrada
     * @throws ResourceNotFoundException si la orden no existe
     */
    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));
        return mapToDTO(order);
    }

    /**
     * Obtiene todas las órdenes con paginación
     * @param pageable Información de paginación
     * @return Página de órdenes
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(this::mapToDTO);
    }

    /**
     * Busca órdenes por cliente (búsqueda parcial)
     * @param customer Nombre o parte del nombre del cliente
     * @param pageable Información de paginación
     * @return Página de órdenes que coinciden con la búsqueda
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByCustomer(String customer, Pageable pageable) {
        return orderRepository.findByCustomerContainingIgnoreCase(customer, pageable).map(this::mapToDTO);
    }

    /**
     * Filtra órdenes por estado
     * @param status Estado a filtrar
     * @param pageable Información de paginación
     * @return Página de órdenes con el estado especificado
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable) {
        return orderRepository.findByStatus(status, pageable).map(this::mapToDTO);
    }

    /**
     * Filtra órdenes por cliente y estado
     * @param customer Nombre o parte del nombre del cliente
     * @param status Estado a filtrar
     * @param pageable Información de paginación
     * @return Página de órdenes que coinciden con ambos criterios
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderDTO> getOrdersByCustomerAndStatus(String customer, OrderStatus status, Pageable pageable) {
        return orderRepository.findByCustomerContainingIgnoreCaseAndStatus(customer, status, pageable)
                .map(this::mapToDTO);
    }

    /**
     * Actualiza el estado de una orden existente
     * @param id ID de la orden a actualizar
     * @param status Nuevo estado
     * @return La orden actualizada
     * @throws ResourceNotFoundException si la orden no existe
     */
    @Override
    @Transactional
    public OrderDTO updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));
        
        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);
        
        return mapToDTO(updatedOrder);
    }
    
    /**
     * Convierte un DTO a una entidad Order
     * Utiliza el patrón Builder
     */
    private Order mapToEntity(OrderDTO orderDTO) {
        Order order = Order.builder()
                .id(orderDTO.getId())
                .customer(orderDTO.getCustomer())
                .date(orderDTO.getDate())
                .status(orderDTO.getStatus())
                .total(orderDTO.getTotal())
                .items(new ArrayList<>())
                .build();
        
        // Mapear items
        if (orderDTO.getItems() != null) {
            List<OrderItem> orderItems = orderDTO.getItems().stream()
                    .map(itemDTO -> {
                        OrderItem item = OrderItem.builder()
                                .productId(itemDTO.getProductId())
                                .productName(itemDTO.getProductName())
                                .quantity(itemDTO.getQuantity())
                                .unitPrice(itemDTO.getUnitPrice())
                                .subtotal(itemDTO.getSubtotal())
                                .order(order)
                                .build();
                        return item;
                    })
                    .collect(Collectors.toList());
            
            order.getItems().addAll(orderItems);
        }
        
        return order;
    }
    
    /**
     * Convierte una entidad Order a un DTO
     * Utiliza el patrón Builder
     */
    private OrderDTO mapToDTO(Order order) {
        OrderDTO orderDTO = OrderDTO.builder()
                .id(order.getId())
                .customer(order.getCustomer())
                .date(order.getDate())
                .status(order.getStatus())
                .total(order.getTotal())
                .items(new ArrayList<>())
                .build();
        
        // Mapear items
        if (order.getItems() != null) {
            List<OrderItemDTO> orderItemDTOs = order.getItems().stream()
                    .map(item -> OrderItemDTO.builder()
                            .id(item.getId())
                            .productId(item.getProductId())
                            .productName(item.getProductName())
                            .quantity(item.getQuantity())
                            .unitPrice(item.getUnitPrice())
                            .subtotal(item.getSubtotal())
                            .build())
                    .collect(Collectors.toList());
            
            orderDTO.getItems().addAll(orderItemDTOs);
        }
        
        return orderDTO;
    }
} 