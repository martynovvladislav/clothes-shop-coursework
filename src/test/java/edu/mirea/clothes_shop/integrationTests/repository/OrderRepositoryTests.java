package edu.mirea.clothes_shop.integrationTests.repository;

import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.entity.Order;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.OrderStatus;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.repository.OrderRepository;
import edu.mirea.clothes_shop.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class OrderRepositoryTests extends IntegrationTest {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderRepositoryTests(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Test
    @Transactional
    @Rollback
    void getOrdersByUserUserIdTest() {
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("mail@mail.mail");
        user.setRole(UserRole.USER);
        user.setPassword("qwerty123");
        user.setOrders(new ArrayList<>());
        userRepository.save(user);
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.IN_PROCESS);
        order.setItems(new ArrayList<>());
        orderRepository.save(order);
        Assertions.assertEquals(1, orderRepository.getOrdersByUserUserId(user.getUserId()).size());
    }

    @Test
    @Transactional
    @Rollback
    void getOrderByUser_UserIdAndStatusTest() {
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("mail@mail.mail");
        user.setRole(UserRole.USER);
        user.setPassword("qwerty123");
        user.setOrders(new ArrayList<>());
        userRepository.save(user);

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.IN_PROCESS);
        order.setItems(new ArrayList<>());
        orderRepository.save(order);
        Assertions.assertTrue(
                orderRepository.getOrderByUser_UserIdAndStatus(user.getUserId(), OrderStatus.ORDERED).isEmpty()
        );

        Order order1 = new Order();
        order1.setUser(user);
        order1.setStatus(OrderStatus.ORDERED);
        order1.setItems(new ArrayList<>());
        orderRepository.save(order1);
        Assertions.assertEquals(
                orderRepository.getOrderByUser_UserIdAndStatus(user.getUserId(), OrderStatus.ORDERED).get(),
                order1
        );
    }
}
