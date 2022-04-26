package com.example.scaffold.mapper;

import com.example.scaffold.model.Order;
import com.example.scaffold.response.OrderDetailsResponse;
import org.mapstruct.Mapper;

import static org.mapstruct.NullValueCheckStrategy.*;
import static org.mapstruct.NullValueMappingStrategy.RETURN_DEFAULT;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = "spring", nullValueMappingStrategy = RETURN_DEFAULT,
        nullValueCheckStrategy = ALWAYS, unmappedSourcePolicy = IGNORE, unmappedTargetPolicy = IGNORE)
public interface OrderMapper {
    OrderDetailsResponse toOrderDetailResponse(Order order);
}
