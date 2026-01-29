package com.gustavo.order_service.dto;

import com.gustavo.order_service.model.Order;

public record CreateOrderResponse(Order order,
String checkoutUrl) {
}
