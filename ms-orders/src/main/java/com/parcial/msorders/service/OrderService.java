package com.parcial.msorders.service;

import com.parcial.msorders.dto.OrderDTO;
import com.parcial.msorders.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    
    OrderDTO createOrder(OrderDTO orderDTO);
    
    OrderDTO getOrderById(Long id);
    
    Page<OrderDTO> getAllOrders(Pageable pageable);
    
    Page<OrderDTO> getOrdersByCustomer(String customer, Pageable pageable);
    
    Page<OrderDTO> getOrdersByStatus(OrderStatus status, Pageable pageable);
    
    Page<OrderDTO> getOrdersByCustomerAndStatus(String customer, OrderStatus status, Pageable pageable);
    
    OrderDTO updateOrderStatus(Long id, OrderStatus status);
} 