package com.parcial.msorders.repository;

import com.parcial.msorders.model.Order;
import com.parcial.msorders.model.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Page<Order> findByCustomerContainingIgnoreCase(String customer, Pageable pageable);
    
    Page<Order> findByStatus(OrderStatus status, Pageable pageable);
    
    Page<Order> findByCustomerContainingIgnoreCaseAndStatus(String customer, OrderStatus status, Pageable pageable);
} 