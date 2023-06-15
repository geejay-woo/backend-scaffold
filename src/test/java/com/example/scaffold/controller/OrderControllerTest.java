package com.example.scaffold.controller;

import com.example.scaffold.request.SaveOrderRequest;
import com.example.scaffold.response.OrderDetailsResponse;
import com.example.scaffold.service.OrderService;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @Test
    public void test() {
        LocalDate startDate = LocalDate.of(2022, 7, 5);
        LocalDate endDate = LocalDate.of(2023, 6, 14);

        long monthsBetween = ChronoUnit.MONTHS.between(startDate.withDayOfMonth(1), endDate.withDayOfMonth(1));
        List<Pair<String, Long>> collect = Stream.iterate(startDate.minusMonths(1).withDayOfMonth(1), date -> date.plusMonths(1)).limit(monthsBetween + 1)
                .map(date -> {
                    LocalDate end = date.plusMonths(1);
                    long count = List.of(LocalDate.of(2022, 7, 10), LocalDate.of(2023, 3, 5))
                            .stream()
                            .filter(it -> it.isAfter(date) && it.isBefore(end))
                            .count();
                    return Pair.of(String.format("%s年%s月", date.getYear(), date.getMonth().getValue()), count);
                }).collect(Collectors.toList());
        for (Pair<String, Long> item : collect) {
            System.out.println(item.getLeft() + "==>>" + item.getRight());
        }
    }

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

    @Test
    public void should_save_success_when_save_order_given_order_info() {
        // given
        String orderCode = "orderCode";
        SaveOrderRequest request = SaveOrderRequest.builder().orderCode(orderCode).build();

        // when
        orderController.postOrder(request);

        // then
        then(orderService).should(times(1)).saveOrder(request);
    }
}