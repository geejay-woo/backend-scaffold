package com.example.scaffold.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SaveOrderRequest {
    private String orderCode;
    private String orderTitle;
    private String description;
    private Long totalPrice;
}
