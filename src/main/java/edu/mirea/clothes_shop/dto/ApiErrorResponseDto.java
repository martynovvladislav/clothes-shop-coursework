package edu.mirea.clothes_shop.dto;

public record ApiErrorResponseDto(
        String message,
        String exception
) {
}
