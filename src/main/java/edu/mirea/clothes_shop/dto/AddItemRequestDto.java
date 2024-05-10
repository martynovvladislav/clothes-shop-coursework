package edu.mirea.clothes_shop.dto;

import edu.mirea.clothes_shop.model.enums.ClothesBrand;
import edu.mirea.clothes_shop.model.enums.ClothesColor;
import edu.mirea.clothes_shop.model.enums.ClothesSize;
import edu.mirea.clothes_shop.model.enums.ClothesType;

public record AddItemRequestDto(
        String name,
        String description,
        ClothesBrand brand,
        ClothesType type,
        ClothesSize size,
        ClothesColor color,
        int amount,
        int price,
        String imgPath
) {

}