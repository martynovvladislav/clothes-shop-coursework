package edu.mirea.clothes_shop.integrationTests.repository;

import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.entity.Item;
import edu.mirea.clothes_shop.model.enums.ClothesBrand;
import edu.mirea.clothes_shop.model.enums.ClothesColor;
import edu.mirea.clothes_shop.model.enums.ClothesSize;
import edu.mirea.clothes_shop.model.enums.ClothesType;
import edu.mirea.clothes_shop.repository.ItemRepository;
import edu.mirea.clothes_shop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class ItemRepositoryTests extends IntegrationTest {
    private final ItemRepository itemRepository;

    @Autowired
    public ItemRepositoryTests(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Test
    @Transactional
    @Rollback
    void findAllByAmountGreaterThanTest() {
        Item item = new Item();
        item.setItemName("nike t-shirt");
        item.setAmount(0);
        item.setOrders(new ArrayList<>());
        item.setColor(ClothesColor.BLUE);
        item.setBrand(ClothesBrand.NIKE);
        item.setPrice(1000);
        item.setDescription("good t-shirt");
        item.setSize(ClothesSize.L);
        item.setType(ClothesType.T_SHIRT);
        item.setImgPath("path");
        itemRepository.save(item);
        Assertions.assertTrue(itemRepository.findAllByAmountGreaterThan(0).isEmpty());

        item.setAmount(1);
        itemRepository.save(item);
        Assertions.assertEquals(1, itemRepository.findAllByAmountGreaterThan(0).size());
    }
}
