package edu.mirea.clothes_shop.model.entity;

import edu.mirea.clothes_shop.model.enums.ClothesBrand;
import edu.mirea.clothes_shop.model.enums.ClothesColor;
import edu.mirea.clothes_shop.model.enums.ClothesSize;
import edu.mirea.clothes_shop.model.enums.ClothesType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long itemId;

    @Column(name = "item_name", nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClothesBrand brand;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClothesType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClothesSize size;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ClothesColor color;

    @Column(nullable = false)
    private int amount;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private String imgPath;

    @OneToMany(
        mappedBy = "item"
    )
    private List<OrderItem> orders;

    public Item(String name, String description, ClothesBrand brand, ClothesType type, ClothesSize size, ClothesColor color, int amount, int price, String imgPath) {
        this.itemName = name;
        this.description = description;
        this.brand = brand;
        this.type = type;
        this.size = size;
        this.color = color;
        this.amount = amount;
        this.price = price;
        this.orders = new ArrayList<>();
        this.imgPath = imgPath;
    }
}
