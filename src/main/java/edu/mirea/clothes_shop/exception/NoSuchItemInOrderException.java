package edu.mirea.clothes_shop.exception;

public class NoSuchItemInOrderException extends RuntimeException {
    public NoSuchItemInOrderException(String message) {
        super(message);
    }
}
