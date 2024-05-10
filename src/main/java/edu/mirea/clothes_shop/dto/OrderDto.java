package edu.mirea.clothes_shop.dto;

import edu.mirea.clothes_shop.model.enums.OrderStatus;

import java.util.List;

public record OrderDto(
        Long orderId,
        OrderStatus status,
        Long userId,
        List<ItemInOrderDto> items
) {
}
