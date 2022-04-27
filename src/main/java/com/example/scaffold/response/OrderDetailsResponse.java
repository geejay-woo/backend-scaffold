package com.example.scaffold.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("订单响应详情")
public class OrderDetailsResponse implements Serializable {

    @ApiModelProperty("订单ID")
    private String orderCode;

    @ApiModelProperty("订单标题")
    private String orderTitle;

    @ApiModelProperty("订单描述信息")
    private String description;

    @ApiModelProperty("订单总结")
    private String totalPrice;

}
