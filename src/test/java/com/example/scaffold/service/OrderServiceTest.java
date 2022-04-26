package com.example.scaffold.service;


import com.example.scaffold.exception.BusinessException;
import com.example.scaffold.mapper.OrderMapper;
import com.example.scaffold.model.Order;
import com.example.scaffold.repository.OrderRepository;
import com.example.scaffold.response.OrderDetailsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Spy
    private OrderMapper orderMapper = Mappers.getMapper(OrderMapper.class);

    @Test
    public void should_return_order_info_when_find_order_by_order_code_given_exist_order_with_order_code() {
        // given
        String orderCode = "orderCode";
        Order order = Order.builder().orderCode(orderCode).id(1L)
                .description("description").orderTitle("orderTitle").build();

        // when
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.of(order));
        OrderDetailsResponse result = orderService.getOrderBy(orderCode);

        // then
        assertThat(result.getOrderCode()).isEqualTo(orderCode);
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getOrderTitle()).isEqualTo("orderTitle");
    }

    @Test
    public void should_throw_exception_when_find_order_by_order_code_given_not_exist_order_with_order_code() {
        // given
        String orderCode = "orderCode";

        // when
        when(orderRepository.findByOrderCode(orderCode)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> orderService.getOrderBy(orderCode));

        // then
        then(orderRepository).should().findByOrderCode(orderCode);
        assertThat(throwable).isInstanceOf(BusinessException.class);
        assertThat(throwable).hasMessage("order cannot be found by order code");
    }
}