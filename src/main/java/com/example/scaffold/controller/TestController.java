package com.example.scaffold.controller;

import com.example.scaffold.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    // InheritableThreadLocal的运行机制是在新建线程的时候,
    // 如果在切换线程的时候, 当前线程没有新建线程(比如使用了一个固定线程池), 则切换线程后, 不一定能获取到这个值
    // 但是形如Schedulers.newSingle("新创建的线程")或直接new Thread()则会在新的线程获取这个值
    private static final InheritableThreadLocal<String> threadLocal = new InheritableThreadLocal<>();

    @GetMapping("inherit")
    public void testInheritableThreadLocal() {
        threadLocal.set("mainThread");
        System.out.println("value:" + threadLocal.get());
//        Thread thread = new Thread(() -> {
//            String value = threadLocal.get();
//            System.out.println("value:" + value);
//        });
//        thread.start();
        Mono.fromRunnable(() -> {
            String result = threadLocal.get();
            System.out.println("value:" + result);
        }).subscribeOn(Schedulers.newSingle("新创建的线程")).block();
        return;
    }

    // 该接口使用线程池进行异步任务, 测试线程池里的线程每次销毁了Context信息的测试：resources/HTTP请求.jmx
    // beanShell脚本中jmeter内置的对象api: https://jmeter.apache.org/api/org/apache/jmeter/protocol/http/control/Header.html
    @GetMapping
    public String testExecutorGetThreadContext() {
        return testService.testExecutorGetThreadContext();
    }
}
