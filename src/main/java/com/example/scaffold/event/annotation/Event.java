package com.example.scaffold.event.annotation;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Event {

    String exchange();

    String routingKey();
}
