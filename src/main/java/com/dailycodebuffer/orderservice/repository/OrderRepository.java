package com.dailycodebuffer.orderservice.repository;

import com.dailycodebuffer.orderservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
