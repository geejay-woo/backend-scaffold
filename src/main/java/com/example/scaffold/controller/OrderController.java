package com.example.scaffold.controller;

import com.example.scaffold.domain.OrderWithParentId;
import com.example.scaffold.request.SaveOrderRequest;
import com.example.scaffold.response.OrderDetailsResponse;
import com.example.scaffold.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/test")
    public ResponseEntity<List<OrderWithParentId>> getOrderDetails2() {
        List<OrderWithParentId> orderDetailsResponse = orderService.getOrderInfo();
        return ResponseEntity.ok(orderDetailsResponse);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "获取订单详情", httpMethod = "GET", response = OrderDetailsResponse.class)
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable("orderId") Long orderId) {
        OrderDetailsResponse orderDetailsResponse = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok(orderDetailsResponse);
    }

    @PostMapping
    @ApiOperation(value = "保存订单信息", httpMethod = "POST")
    public ResponseEntity postOrder(@RequestBody @Valid SaveOrderRequest request) {
        orderService.saveOrder(request);
        return ResponseEntity.noContent().build();
    }
}
