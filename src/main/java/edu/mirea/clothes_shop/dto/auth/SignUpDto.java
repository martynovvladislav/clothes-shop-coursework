package edu.mirea.clothes_shop.dto.auth;

import edu.mirea.clothes_shop.model.enums.UserRole;

public record SignUpDto(
        String email,
        String firstName,
        String lastName,
        String password
) {
}
