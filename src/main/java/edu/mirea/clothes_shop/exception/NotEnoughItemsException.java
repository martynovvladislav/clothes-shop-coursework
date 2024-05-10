package edu.mirea.clothes_shop.exception;

public class NotEnoughItemsException extends RuntimeException {
    public NotEnoughItemsException(String message) {
        super(message);
    }
}
