package edu.mirea.clothes_shop.integrationTests.service;

import edu.mirea.clothes_shop.dto.AddItemRequestDto;
import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.*;
import edu.mirea.clothes_shop.repository.OrderRepository;
import edu.mirea.clothes_shop.repository.UserRepository;
import edu.mirea.clothes_shop.service.ItemService;
import edu.mirea.clothes_shop.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTests extends IntegrationTest {
    private final OrderService orderService;
    private final UserRepository userRepository;
    private final ItemService itemService;
    private final OrderRepository orderRepository;
    private final static String EMAIL = "email";

    @Autowired
    public OrderServiceTests(OrderService orderService,
                             UserRepository userRepository,
                             ItemService itemService,
                             OrderRepository orderRepository) {
        this.orderService = orderService;
        this.userRepository = userRepository;
        this.itemService = itemService;
        this.orderRepository = orderRepository;
    }

    @BeforeAll
    public void init() {
        User user = new User();
        user.setRole(UserRole.USER);
        user.setOrders(new ArrayList<>());
        user.setPassword("password");
        user.setEmail(EMAIL);
        user.setFirstName("name");
        user.setLastName("surname");
        userRepository.save(user);
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
    }

    @Test
    @Transactional
    void addItemInOrderTest() {
        Long itemId = itemService.getItems().get(0).id();
        orderService.addItemInCurrentOrder(EMAIL, itemId);
        Assertions.assertEquals(orderService.getCurrentOrder(EMAIL).items().size(), 1);
    }

    @Test
    @Transactional
    void deleteItemFromOrderTest() {
        Long itemId = itemService.getItems().get(0).id();
        orderService.addItemInCurrentOrder(EMAIL, itemId);
        orderService.deleteItemFromCurrentOrder(EMAIL, itemId);
        Assertions.assertEquals(orderService.getCurrentOrder(EMAIL).items().size(), 0);
    }

    @Test
    @Transactional
    void getCurrentOrderTest() {
        Long itemId = itemService.getItems().get(0).id();
        orderService.addItemInCurrentOrder(EMAIL, itemId);
        Assertions.assertEquals(
                orderService.getCurrentOrder(EMAIL).items().get(0).name(),
                "some item"
        );
    }

    @Test
    @Transactional
    void makeOrderTest() {
        Long itemId = itemService.getItems().get(0).id();
        orderService.addItemInCurrentOrder(EMAIL, itemId);
        Long orderId = orderService.getCurrentOrder(EMAIL).orderId();
        Assertions.assertEquals(orderRepository.findById(orderId).get().getStatus(), OrderStatus.IN_PROCESS);

        orderService.makeOrder(EMAIL);
        Assertions.assertEquals(orderRepository.findById(orderId).get().getStatus(), OrderStatus.ORDERED);
        Assertions.assertEquals(orderService.getCurrentOrder(EMAIL).status(), OrderStatus.IN_PROCESS);
    }
}
