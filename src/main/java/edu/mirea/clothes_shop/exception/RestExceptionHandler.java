package edu.mirea.clothes_shop.exception;

import edu.mirea.clothes_shop.dto.ApiErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Objects;

@RestControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler({
            ItemDoesNotExistException.class,
            NoSuchItemInOrderException.class,
            OrderIsEmptyException.class,
            UserAlreadyExistException.class,
            UserDoesNotExistException.class,
            NotEnoughItemsException.class
    })
    public ResponseEntity<ApiErrorResponseDto> itemDoesNotExist(Exception e) {
        return new ResponseEntity<>(
                new ApiErrorResponseDto(
                        e.getLocalizedMessage(),
                        e.getClass().getSimpleName()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiErrorResponseDto> argumentMismatch(MethodArgumentTypeMismatchException e) {
        String name = e.getName();
        String type = Objects.requireNonNull(e.getRequiredType()).getSimpleName();
        Object value = e.getValue();
        return new ResponseEntity<>(
                new ApiErrorResponseDto(
                        name + " should be a valid: " + type + ", but it was " + value,
                        e.getClass().getSimpleName()
                ),
                HttpStatus.BAD_REQUEST
        );
    }
}
