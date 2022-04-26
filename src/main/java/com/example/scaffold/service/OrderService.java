package com.example.scaffold.service;

import com.example.scaffold.exception.BusinessException;
import com.example.scaffold.exception.ErrorCodes;
import com.example.scaffold.mapper.OrderMapper;
import com.example.scaffold.repository.OrderRepository;
import com.example.scaffold.response.OrderDetailsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    public OrderDetailsResponse getOrderBy(String orderCode) {
        return orderRepository.findByOrderCode(orderCode)
                .map(orderMapper::toOrderDetailResponse)
                .orElseThrow(() -> new BusinessException(ErrorCodes.ORDER_CANNOT_FOUND));
    }
}
