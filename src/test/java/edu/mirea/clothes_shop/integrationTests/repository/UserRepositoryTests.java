package edu.mirea.clothes_shop.integrationTests.repository;

import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class UserRepositoryTests extends IntegrationTest {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTests(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    @Transactional
    @Rollback
    void findByEmailTest() {
        User user = new User();
        user.setFirstName("Ivan");
        user.setLastName("Ivanov");
        user.setEmail("mail@mail.mail");
        user.setRole(UserRole.USER);
        user.setPassword("qwerty123");
        user.setOrders(new ArrayList<>());
        userRepository.save(user);
        Assertions.assertTrue(userRepository.findByEmail("mail@mail.mail").isPresent());
    }
}
