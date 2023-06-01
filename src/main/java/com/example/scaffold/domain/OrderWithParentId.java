package com.example.scaffold.domain;

import com.example.scaffold.model.master.Order;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWithParentId {
    private Order orderInfo;
    private Long parentOrderId;
}
