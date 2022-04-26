package com.example.scaffold.controller;

import com.example.scaffold.response.OrderDetailsResponse;
import com.example.scaffold.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Test
    public void should_return_order_details_when_get_order_given_order_is_exist() {
        // given
        Long orderId = 1L;
        String description = "description";
        String orderCode = "code";
        OrderDetailsResponse response = OrderDetailsResponse.builder()
                .orderCode(orderCode).description(description).build();

        // when
        when(orderService.getOrderByOrderId(orderId)).thenReturn(response);
        ResponseEntity<OrderDetailsResponse> result = orderController.getOrderDetails(orderId);

        // then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result).isNotNull();
        assertThat(result.getBody().getOrderCode()).isEqualTo(orderCode);
        assertThat(result.getBody().getDescription()).isEqualTo(description);
    }
}