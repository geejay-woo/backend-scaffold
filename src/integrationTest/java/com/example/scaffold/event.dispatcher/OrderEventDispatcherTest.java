package com.example.scaffold.event.dispatcher;

import com.example.scaffold.event.domain.Operation;
import com.example.scaffold.event.domain.OrderEvent;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class OrderEventDispatcherTest {
    @Autowired
    private EventDispatcher orderEventDispatcher;

    @Test
    public void should_send_message() {
        OrderEvent event = OrderEvent.builder().orderCode("code1000").orderTitle("title")
                .operation(Operation.SAVE).description("description").totalPrice(100L).build();
        orderEventDispatcher.dispatcher(event);
    }

    @Test
    public void should_update_message() {
        OrderEvent event = OrderEvent.builder().id(1L).orderCode("modified").orderTitle("title")
                .operation(Operation.SAVE).description("description").totalPrice(100L).build();
        orderEventDispatcher.dispatcher(event);
    }
}