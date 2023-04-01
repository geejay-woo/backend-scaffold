package com.example.scaffold;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

@Component
public class TransmitRequestExecutor extends ThreadPoolTaskExecutor {

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return super.submit(() -> {
            try {
                RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
                return task.call();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        });
    }

    @Override
    public void execute(Runnable task) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        super.execute(() -> {
            try {
                RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
                task.run();
            } finally {
                RequestContextHolder.resetRequestAttributes();
            }
        });
    }
}