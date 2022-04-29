package com.example.scaffold.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Builder
public class SaveOrderRequest {
    @NotBlank
    private String orderCode;
    private String orderTitle;
    private String description;
    @NotNull
    private Long totalPrice;
}
