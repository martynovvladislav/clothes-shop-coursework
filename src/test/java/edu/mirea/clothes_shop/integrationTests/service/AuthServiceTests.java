package edu.mirea.clothes_shop.integrationTests.service;

import edu.mirea.clothes_shop.dto.auth.SignUpDto;
import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.repository.UserRepository;
import edu.mirea.clothes_shop.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class AuthServiceTests extends IntegrationTest {
    private final AuthService authService;
    private final UserRepository userRepository;

    @Autowired
    public AuthServiceTests(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @Test
    @Transactional
    @Rollback
    void signUpTest() {
        SignUpDto data = new SignUpDto(
                "mail@mail.com",
                "Ivan",
                "Ivanov",
                "qwerty11"
        );
        UserDetails userDetails = authService.signUp(data);
        Assertions.assertTrue(userRepository.findByEmail(userDetails.getUsername()).isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void loadUserTest() {
        SignUpDto data = new SignUpDto(
                "mail@mail.com",
                "Ivan",
                "Ivanov",
                "qwerty11"
        );
        UserDetails userDetails = authService.signUp(data);
        Assertions.assertEquals(authService.loadUserByUsername("mail@mail.com"), userDetails);
    }
}
