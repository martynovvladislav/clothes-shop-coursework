package edu.mirea.clothes_shop.integrationTests.controller;

import edu.mirea.clothes_shop.integrationTests.IntegrationTest;
import edu.mirea.clothes_shop.service.OrderService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {"spring.jpa.hibernate.ddl-auto=create-drop"})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderControllerSecurityTests extends IntegrationTest {
    @Autowired
    private WebApplicationContext context;

    @MockBean
    private OrderService orderService;

    private MockMvc mockMvc;

    @BeforeAll
    public void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithAnonymousUser
    void anonymousUserCannotGetOrders() throws Exception {
        when(orderService.getOrders(anyString()))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/v1/orders")).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCanGetOrders() throws Exception {
        when(orderService.getOrders(anyString()))
                .thenReturn(new ArrayList<>());
        mockMvc.perform(get("/api/v1/orders")).andExpect(status().isOk());
    }
}