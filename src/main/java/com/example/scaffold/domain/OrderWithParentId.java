package com.example.scaffold.domain;

import com.example.scaffold.model.master.Order;
import com.example.scaffold.model.master.OrderUser;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithParentId {
    private Order orderInfo;
    private OrderUser orderUser;
    private Long parentOrderId;
}
