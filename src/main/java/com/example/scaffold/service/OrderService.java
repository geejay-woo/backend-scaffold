package com.example.scaffold.service;

import com.example.scaffold.exception.BusinessException;
import com.example.scaffold.exception.ErrorCodes;
import com.example.scaffold.mapper.OrderMapper;
import com.example.scaffold.model.Order;
import com.example.scaffold.repository.OrderRepository;
import com.example.scaffold.request.SaveOrderRequest;
import com.example.scaffold.response.OrderDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderDetailsResponse getOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderDetailResponse)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ORDER_CANNOT_FOUND));
    }

    public void saveOrder(SaveOrderRequest request) {
        Order order = orderMapper.toOrder(request);
        orderRepository.save(order);
    }
}
