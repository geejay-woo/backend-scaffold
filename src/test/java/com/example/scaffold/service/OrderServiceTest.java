package com.example.scaffold.service;


import com.example.scaffold.exception.BusinessException;
import com.example.scaffold.mapper.OrderMapper;
import com.example.scaffold.model.Order;
import com.example.scaffold.repository.OrderRepository;
import com.example.scaffold.request.SaveOrderRequest;
import com.example.scaffold.response.OrderDetailsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;
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
        Long orderId = 1L;
        String orderCode = "orderCode";
        Order order = Order.builder().orderCode(orderCode).id(1L)
                .description("description").orderTitle("orderTitle").build();

        // when
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        OrderDetailsResponse result = orderService.getOrderByOrderId(orderId);

        // then
        assertThat(result.getOrderCode()).isEqualTo(orderCode);
        assertThat(result.getDescription()).isEqualTo("description");
        assertThat(result.getOrderTitle()).isEqualTo("orderTitle");
    }

    @Test
    public void should_throw_exception_when_find_order_by_order_code_given_not_exist_order_with_order_code() {
        // given
        Long orderId = 1L;
        String orderCode = "orderCode";

        // when
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());
        Throwable throwable = catchThrowable(() -> orderService.getOrderByOrderId(orderId));

        // then
        then(orderRepository).should().findById(orderId);
        assertThat(throwable).isInstanceOf(BusinessException.class);
        assertThat(throwable).hasMessage("order cannot be found by order code");
    }

    @Test
    public void should_save_order_when_save_given_order_info() {
        // given
        String orderCode = "orderCode";
        String orderTitle = "orderTitle";
        SaveOrderRequest request = SaveOrderRequest.builder().orderCode(orderCode).orderTitle(orderTitle).build();
        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        // when
        when(orderRepository.save(orderCaptor.capture())).thenReturn(null);
        orderService.saveOrder(request);

        // then
        assertThat(orderCaptor.getValue().getOrderCode()).isEqualTo(orderCode);
        assertThat(orderCaptor.getValue().getOrderTitle()).isEqualTo(orderTitle);
    }

}