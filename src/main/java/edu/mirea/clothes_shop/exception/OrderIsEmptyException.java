package edu.mirea.clothes_shop.exception;

public class OrderIsEmptyException extends RuntimeException {
    public OrderIsEmptyException(String message) {
        super(message);
    }
}
