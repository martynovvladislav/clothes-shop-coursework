package edu.mirea.clothes_shop.dto;

import java.util.List;

public record UserWithOrdersDto(
        UserDto userDto,
        List<OrderDto> orders
) {
}
