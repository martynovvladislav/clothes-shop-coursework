package edu.mirea.clothes_shop.integrationTests.service;

import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.model.entity.User;
import edu.mirea.clothes_shop.model.enums.UserRole;
import edu.mirea.clothes_shop.service.TokenProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
public class TokenProviderTests extends IntegrationTest {
    private final TokenProvider tokenProvider;

    @Autowired
    public TokenProviderTests(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Test
    void tokenTest() {
        User user = new User();
        user.setRole(UserRole.USER);
        user.setOrders(new ArrayList<>());
        user.setPassword("password");
        user.setEmail("email");
        user.setFirstName("name");
        user.setLastName("surname");

        String token = tokenProvider.generateAccessToken(user);
        Assertions.assertEquals(tokenProvider.validateToken(token), user.getEmail());
    }
}
