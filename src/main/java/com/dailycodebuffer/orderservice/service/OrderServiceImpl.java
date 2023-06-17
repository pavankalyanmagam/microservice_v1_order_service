package com.dailycodebuffer.orderservice.service;


import com.dailycodebuffer.orderservice.entity.Order;
import com.dailycodebuffer.orderservice.exception.CustomException;
import com.dailycodebuffer.orderservice.external.client.PaymentService;
import com.dailycodebuffer.orderservice.external.client.ProductService;
import com.dailycodebuffer.orderservice.external.request.PaymentRequest;
import com.dailycodebuffer.orderservice.mo.OrderResponse;
import com.dailycodebuffer.orderservice.model.OrderRequest;
import com.dailycodebuffer.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService{


    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    private final OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity -> Save the data with Status Order Created

        // Product Service -> Block Products (Reduce the Quantity)

        // Payment Service - > Payment -> Success -> COMPLETE - Else -> Failed


        log.info("Placing Order Request {}", orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");
        Order order = Order.builder()

                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);


        log.info("Calling Payment Service to complete the Payment");


        PaymentRequest paymentRequest = PaymentRequest.builder()

                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())

                .build();


        String orderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment done Successfully. Changing the Order Status to Placed");
            orderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Error occurred in payment. Changing order status to FAILED");
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);

        orderRepository.save(order);


        log.info("Order Placed Successfully with order Id: {}", order.getId());



        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(Long orderId) {

        log.info("Get order details for Order Id: {}", orderId);

         Order order = orderRepository.findById(orderId).orElseThrow(() -> new CustomException("Order with the give id not found","NOT_FOUND", 404));


         OrderResponse orderResponse = OrderResponse.builder()

                 .orderId(order.getId())
                 .orderStatus(order.getOrderStatus())
                 .amount(order.getAmount())
                 .orderDate(order.getOrderDate())
                 .build();
        return orderResponse;
    }
}
