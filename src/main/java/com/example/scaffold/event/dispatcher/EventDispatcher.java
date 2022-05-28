package com.example.scaffold.event.dispatcher;

public interface EventDispatcher<T> {
    void dispatcher(T event);
}
