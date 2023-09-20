package com.microservices.orders_service.services;


import com.microservices.orders_service.model.dtos.*;
import com.microservices.orders_service.model.entities.Order;
import com.microservices.orders_service.model.entities.OrderItems;
import com.microservices.orders_service.repositories.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) {

        //Check for inventory

        BaseResponse result = this.webClientBuilder.build()
                .post()
                .uri("http://localhost:8083/api/inventory/in-stock")
                .retrieve()
                .bodyToMono(BaseResponse.class)
                .block();
        if (result != null && !result.hasErrors()) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setOrderItems(orderRequest.getOrderItems().stream()
                .map(orderItemsRequest -> mapOrderItemsRequestToOrderItem(orderItemsRequest, order))
                .toList());
        this.orderRepository.save(order);
        }else {
            throw new IllegalStateException("Sone of the products are not in stock");
        }
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = this.orderRepository.findAll();

        return orders.stream().map(this::mapToOrderResponse).toList();
    }

    private OrderResponse mapToOrderResponse(Order order) {
        return new OrderResponse(order.getId(), order.getOrderNumber()
                , order.getOrderItems().stream().map(this::mapToOrderItemRequest).toList());
    }

    private OrderItemsResponse mapToOrderItemRequest(OrderItems orderItems) {
        return new OrderItemsResponse(orderItems.getId(), orderItems.getSku(), orderItems.getPrice(), orderItems.getQuantity());
    }

    private OrderItems mapOrderItemsRequestToOrderItem(OrderItemsRequest orderItemsRequest, Order order) {
        return OrderItems.builder()
                .id(orderItemsRequest.getId())
                .sku(orderItemsRequest.getSku())
                .price(orderItemsRequest.getPrice())
                .quantity(orderItemsRequest.getQuantity())
                .order(order)
                .build();
    }
}
