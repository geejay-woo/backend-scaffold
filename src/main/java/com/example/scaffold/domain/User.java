package com.example.scaffold.domain;

import com.alibaba.fastjson.JSON;
import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String name;
    private String code;

    public static User parseFromJson(String userJson) {
        return JSON.parseObject(userJson, User.class);
    }
}
