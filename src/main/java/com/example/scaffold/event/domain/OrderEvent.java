package com.example.scaffold.event.domain;

import com.example.scaffold.event.annotation.Event;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Event(exchange = QueueConstant.ONLINE_ORDER_EXCHANGE, routingKey = QueueConstant.ORDER_UPDATE_ROUTING_KEY)
public class OrderEvent {
    private Long id;

    private String orderCode;

    private String description;

    private String orderTitle;

    private Long totalPrice;

    private Operation operation;
}
