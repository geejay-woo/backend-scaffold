package com.example.scaffold.service;

import com.example.scaffold.response.OrderDetailsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderIntegrationTest {
    @Autowired
    private OrderService orderService;

    @Test
    public void should_get_order() {
        OrderDetailsResponse response = orderService.getOrderByOrderId(1L);
        assertThat(response.getOrderCode()).isNotBlank();
    }
}
