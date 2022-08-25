package com.example.scaffold.mapper;

import com.example.scaffold.event.domain.OrderEvent;
import com.example.scaffold.model.master.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.time.LocalDateTime;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT,
        nullValueCheckStrategy = ALWAYS, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE,
        imports = {LocalDateTime.class})
public interface OrderEventMapper {
    @Mappings({
            @Mapping(target = "creator", expression = "java(String.valueOf(\"mq\"))"),
            @Mapping(target = "modifier", expression = "java(String.valueOf(\"mq\"))"),
            @Mapping(target = "createTime", expression = "java(LocalDateTime.now())"),
            @Mapping(target = "updateTime", expression = "java(LocalDateTime.now())"),
    })
    Order toOrder(OrderEvent orderEvent);
}
