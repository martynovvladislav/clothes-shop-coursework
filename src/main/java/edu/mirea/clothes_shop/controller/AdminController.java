package edu.mirea.clothes_shop.controller;

import edu.mirea.clothes_shop.dto.*;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.service.ItemService;
import edu.mirea.clothes_shop.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final ItemService itemService;
    private final UserService userService;

    @PostMapping
    public void addItem(@RequestBody AddItemRequestDto addItemRequestDto) {
        itemService.addItem(addItemRequestDto);
    }

    @GetMapping("/items")
    public List<ItemDto> getItems() {
        return itemService.getAllItems();
    }

    @PostMapping("/fill")
    public void fillItem(@RequestBody FillItemRequestDto fillItemRequestDto) {
        itemService.fillItem(fillItemRequestDto);
    }

    @GetMapping("/users")
    public List<UserDto> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/users/{id}")
    public UserWithOrdersDto getUser(
            @PathVariable("id") Long userId
    ) {
        return userService.getUser(userId);
    }

    @PostMapping("/users/{id}")
    public void grantUser(
            @PathVariable("id") Long userId,
            @RequestParam("role") UserRole newRole
    ) {
        userService.grantUser(userId, newRole);
    }
}
