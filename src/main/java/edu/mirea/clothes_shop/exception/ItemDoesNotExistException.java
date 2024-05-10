package edu.mirea.clothes_shop.exception;

public class ItemDoesNotExistException extends RuntimeException {
    public ItemDoesNotExistException(String message) {
        super(message);
    }
}
