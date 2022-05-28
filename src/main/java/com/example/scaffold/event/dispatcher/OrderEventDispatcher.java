package com.example.scaffold.event.dispatcher;

import com.example.scaffold.event.annotation.Event;
import com.example.scaffold.event.domain.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(prefix = "application.mq", name = "enabled", havingValue = "true")
@RequiredArgsConstructor
public class OrderEventDispatcher implements EventDispatcher<OrderEvent> {

    private final AmqpTemplate amqpTemplate;

    @Override
    public void dispatcher(OrderEvent event) {
        final Event annotation = AnnotationUtils.findAnnotation(event.getClass(), Event.class);
        amqpTemplate.convertAndSend(annotation.exchange(), annotation.routingKey(), event);
    }
}
