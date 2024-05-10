package edu.mirea.clothes_shop.controller;

import edu.mirea.clothes_shop.dto.OrderDto;
import edu.mirea.clothes_shop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getUserOrders() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return orderService.getOrders(user.getUsername());
    }

    @PostMapping
    public ResponseEntity<Void> addItemInCurrentOrder(
            @RequestParam("item_id") Long itemId
    ) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.addItemInCurrentOrder(user.getUsername(), itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteItemFromCurrentOrder(
            @RequestParam("item_id") Long itemId
    ) {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.deleteItemFromCurrentOrder(user.getUsername(), itemId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/current")
    public ResponseEntity<OrderDto> getCurrentOrder() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ResponseEntity.ok(orderService.getCurrentOrder(user.getUsername()));
    }

    @PostMapping("/current")
    public ResponseEntity<Void> makeOrder() {
        UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderService.makeOrder(user.getUsername());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
