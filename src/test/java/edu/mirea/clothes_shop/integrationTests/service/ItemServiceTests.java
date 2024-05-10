package edu.mirea.clothes_shop.integrationTests.service;

import edu.mirea.clothes_shop.dto.AddItemRequestDto;
import edu.mirea.clothes_shop.dto.FillItemRequestDto;
import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.enums.ClothesBrand;
import edu.mirea.clothes_shop.model.enums.ClothesColor;
import edu.mirea.clothes_shop.model.enums.ClothesSize;
import edu.mirea.clothes_shop.model.enums.ClothesType;
import edu.mirea.clothes_shop.service.ItemService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class ItemServiceTests extends IntegrationTest {
    private final ItemService itemService;

    @Autowired
    public ItemServiceTests(ItemService itemService) {
        this.itemService = itemService;
    }

    @Test
    @Transactional
    @Rollback
    void addAndGetItemsTest() {
        itemService.addItem(
                new AddItemRequestDto(
                        "some item",
                        "description",
                        ClothesBrand.NIKE,
                        ClothesType.SHIRT,
                        ClothesSize.XL,
                        ClothesColor.BLUE,
                        5,
                        1000,
                        "path"
                )
        );

        Assertions.assertEquals(itemService.getItems().size(), 1);
        Assertions.assertEquals(itemService.getItems().get(0).name(), "some item");
    }

    @Test
    @Transactional
    @Rollback
    void onlyAdminCanSeeUnavailableItemsTest() {
        itemService.addItem(
                new AddItemRequestDto(
                        "some item",
                        "description",
                        ClothesBrand.NIKE,
                        ClothesType.SHIRT,
                        ClothesSize.XL,
                        ClothesColor.BLUE,
                        0,
                        1000,
                        "path"
                )
        );

        Assertions.assertEquals(itemService.getAllItems().size(), 1);
        Assertions.assertEquals(itemService.getItems().size(), 0);
    }

    @Test
    @Transactional
    @Rollback
    void fillItemsTest() {
        itemService.addItem(
                new AddItemRequestDto(
                        "some item",
                        "description",
                        ClothesBrand.NIKE,
                        ClothesType.SHIRT,
                        ClothesSize.XL,
                        ClothesColor.BLUE,
                        0,
                        1000,
                        "path"
                )
        );
        Assertions.assertEquals(itemService.getItems().size(), 0);
        Long id = itemService.getAllItems().get(0).id();

        itemService.fillItem(
                new FillItemRequestDto(
                        id,
                        5
                )
        );
        Assertions.assertEquals(itemService.getItems().size(), 1);
        Assertions.assertEquals(itemService.getItems().get(0).amount(), 5);
    }

    @Test
    @Transactional
    @Rollback
    void filterItemsTest() {
        itemService.addItem(
                new AddItemRequestDto(
                        "some item",
                        "description",
                        ClothesBrand.NIKE,
                        ClothesType.SHIRT,
                        ClothesSize.XL,
                        ClothesColor.BLUE,
                        5,
                        1000,
                        "path"
                )
        );
        itemService.addItem(
                new AddItemRequestDto(
                        "some item 1",
                        "description 1",
                        ClothesBrand.ADIDAS,
                        ClothesType.T_SHIRT,
                        ClothesSize.XL,
                        ClothesColor.BLACK,
                        5,
                        1000,
                        "path"
                )
        );

        Assertions.assertEquals(
                itemService.getFilteredItems(null, null, null, null).size(),
                2
        );
        Assertions.assertEquals(
                itemService.getFilteredItems(
                        ClothesBrand.NIKE, ClothesColor.BLUE, ClothesSize.XL, ClothesType.SHIRT
                ).size(),
                1
        );
        Assertions.assertEquals(
                itemService.getFilteredItems(
                        ClothesBrand.ADIDAS, ClothesColor.BLACK, ClothesSize.XL, ClothesType.T_SHIRT
                ).size(),
                1
        );
        Assertions.assertEquals(
                itemService.getFilteredItems(
                        null, null, ClothesSize.XL, null
                ).size(),
                2
        );
        Assertions.assertEquals(
                itemService.getFilteredItems(
                        ClothesBrand.NIKE, null, ClothesSize.XL, null
                ).get(0).name(),
                "some item"
        );
        Assertions.assertEquals(
                itemService.getFilteredItems(
                        ClothesBrand.ADIDAS, null, ClothesSize.XL, null
                ).get(0).name(),
                "some item 1"
        );
    }

    @Test
    @Transactional
    @Rollback
    void getSingleItemTest() {
        itemService.addItem(
                new AddItemRequestDto(
                        "some item",
                        "description",
                        ClothesBrand.NIKE,
                        ClothesType.SHIRT,
                        ClothesSize.XL,
                        ClothesColor.BLUE,
                        5,
                        1000,
                        "path"
                )
        );

        Long id = itemService.getItems().get(0).id();

        Assertions.assertEquals(itemService.getItem(id).name(), "some item");
    }
}
