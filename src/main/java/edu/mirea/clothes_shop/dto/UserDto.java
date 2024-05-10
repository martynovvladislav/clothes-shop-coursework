package edu.mirea.clothes_shop.dto;

import edu.mirea.clothes_shop.model.enums.UserRole;

public record UserDto(
        Long userId,
        String firstName,
        String lastName,
        String email,
        UserRole role
) {
}
