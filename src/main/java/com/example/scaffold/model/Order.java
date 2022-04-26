package com.example.scaffold.model;

import lombok.*;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private Long id;
    private String orderCode;
    private String description;
    private String orderTitle;
    private Long totalPrice;
}

