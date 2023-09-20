package com.microservices.orders_service.model.dtos;


import com.microservices.orders_service.model.entities.OrderItems;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    private List<OrderItemsRequest> orderItems;
}
