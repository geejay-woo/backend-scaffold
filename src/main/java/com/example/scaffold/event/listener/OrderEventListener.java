package com.example.scaffold.event.listener;

import com.alibaba.fastjson.JSON;
import com.example.scaffold.event.domain.Operation;
import com.example.scaffold.event.domain.OrderEvent;
import com.example.scaffold.mapper.OrderEventMapper;
import com.example.scaffold.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import static com.example.scaffold.event.domain.QueueConstant.*;
import static org.springframework.amqp.core.ExchangeTypes.TOPIC;

@Slf4j
@Service
@ConditionalOnProperty(prefix = "application.mq", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class OrderEventListener {
    private final OrderRepository orderRepository;
    private final OrderEventMapper orderEventMapper;

    @RabbitListener(bindings = {@QueueBinding(value = @Queue(ORDER_UPDATE_QUEUE),
            exchange = @Exchange(value = ONLINE_ORDER_EXCHANGE, type = TOPIC), key = ORDER_UPDATE_ROUTING_KEY)})
    public void syncOrderData(OrderEvent orderEvent) {
        log.info("receive order update message: [{}]", JSON.toJSONString(orderEvent));
        if (Operation.UPDATE.equals(orderEvent.getOperation())) {
            orderRepository.save(orderEventMapper.toOrder(orderEvent));
        }
        if (Operation.SAVE.equals(orderEvent.getOperation())) {
            orderRepository.save(orderEventMapper.toOrder(orderEvent));
        }
    }
}
