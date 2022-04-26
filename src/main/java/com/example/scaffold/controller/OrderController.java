package com.example.scaffold.controller;

import com.example.scaffold.response.OrderDetailsResponse;
import com.example.scaffold.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{orderId}")
    @ApiOperation(value = "获取订单详情", httpMethod = "GET", response = OrderDetailsResponse.class)
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable("orderId")Long orderId) {
        OrderDetailsResponse orderDetailsResponse = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok(orderDetailsResponse);
    }
}
