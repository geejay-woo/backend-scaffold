package com.example.scaffold.service;

import com.example.scaffold.TransmitRequestExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@RequiredArgsConstructor
public class TestService {

    private final TransmitRequestExecutor transmitRequestExecutor;

    public String testExecutorGetThreadContext() {
        String user = Mono.fromCallable(() -> {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes.getRequest().getHeader("user");
        }).subscribeOn(Schedulers.fromExecutor(transmitRequestExecutor)).block();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String user2 = attributes.getRequest().getHeader("user");
        return user + "::" + user2;
    }
}
