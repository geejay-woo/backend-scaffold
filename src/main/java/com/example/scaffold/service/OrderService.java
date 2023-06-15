package com.example.scaffold.service;

import com.example.scaffold.domain.OrderWithParentId;
import com.example.scaffold.exception.BusinessException;
import com.example.scaffold.exception.ErrorCodes;
import com.example.scaffold.mapper.OrderMapper;
import com.example.scaffold.model.master.Order;
import com.example.scaffold.repository.OrderRepository;
import com.example.scaffold.repository.OrderTemplateMappingRepository;
import com.example.scaffold.request.SaveOrderRequest;
import com.example.scaffold.response.OrderDetailsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;

    private final OrderTemplateMappingRepository orderTemplateMappingRepository;

    @Cacheable("orders")
    public OrderDetailsResponse getOrderByOrderId(Long orderId) {
        return orderRepository.findById(orderId)
                .map(orderMapper::toOrderDetailResponse)
                .orElseThrow(() -> {
                    log.error("orderId not found, orderId: {}", orderId);
                    return new BusinessException(ErrorCodes.ORDER_CANNOT_FOUND);
                });
    }

    public void saveOrder(SaveOrderRequest request) {
        Order order = orderMapper.toOrder(request);
        orderRepository.save(order);
    }

    public List<OrderWithParentId> getOrderInfo() {
        return orderRepository.getOrderInfos(1L);
    }
}
