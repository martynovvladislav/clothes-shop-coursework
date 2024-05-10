package edu.mirea.clothes_shop.integrationTests.service;

import edu.mirea.clothes_shop.dto.UserWithOrdersDto;
import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.repository.UserRepository;
import edu.mirea.clothes_shop.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class UserServiceTests extends IntegrationTest {
    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserServiceTests(UserService userService, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @BeforeAll
    public void init() {
        User user = new User();
        user.setRole(UserRole.USER);
        user.setOrders(new ArrayList<>());
        user.setPassword("password");
        user.setEmail("email123");
        user.setFirstName("name");
        user.setLastName("surname");
        userRepository.save(user);
    }

    @Test
    @Transactional
    @Rollback
    void getUsersTest() {
        Assertions.assertEquals(userService.getUsers().size(), 2);
    }

    @Test
    @Transactional
    @Rollback
    void getUserTest() {
        UserWithOrdersDto user = userService.getUser(userService.getUsers().get(0).userId());
        Assertions.assertEquals(user.userDto().email(), "email");
    }

    @Test
    @Transactional
    @Rollback
    void grantUserTest() {
        Long userId = userService.getUsers().get(0).userId();
        Assertions.assertEquals(userRepository.findById(userId).get().getRole(), UserRole.USER);

        userService.grantUser(userId, UserRole.ADMIN);
        Assertions.assertEquals(userRepository.findById(userId).get().getRole(), UserRole.ADMIN);
    }
}
