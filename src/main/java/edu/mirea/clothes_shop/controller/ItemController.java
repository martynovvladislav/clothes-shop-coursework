package edu.mirea.clothes_shop.controller;

import edu.mirea.clothes_shop.dto.ItemDto;
import edu.mirea.clothes_shop.model.enums.ClothesBrand;
import edu.mirea.clothes_shop.model.enums.ClothesColor;
import edu.mirea.clothes_shop.model.enums.ClothesSize;
import edu.mirea.clothes_shop.model.enums.ClothesType;
import edu.mirea.clothes_shop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getItems() {
        return new ResponseEntity<>(
                itemService.getItems(),
                HttpStatus.OK
        );
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ItemDto>> getItems(
            @RequestParam(value = "brand", required = false) ClothesBrand brand,
            @RequestParam(value = "color", required = false) ClothesColor color,
            @RequestParam(value = "size", required = false) ClothesSize size,
            @RequestParam(value = "type", required = false) ClothesType type
    ) {
        return new ResponseEntity<>(
                itemService.getFilteredItems(brand, color, size, type),
                HttpStatus.OK
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDto> getItem(
            @PathVariable("id") Long itemId
    ) {
       return new ResponseEntity<>(
               itemService.getItem(itemId),
               HttpStatus.OK
       );
    }
}
