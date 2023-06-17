package com.dailycodebuffer.orderservice.service;

import com.dailycodebuffer.orderservice.mo.OrderResponse;
import com.dailycodebuffer.orderservice.model.OrderRequest;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(Long orderId);
}
