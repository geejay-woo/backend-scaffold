package com.example.scaffold.repository;

import com.example.scaffold.domain.OrderWithParentId;
import com.example.scaffold.model.master.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "select new com.example.scaffold.domain.OrderWithParentId(o, ou, otm.parentOrderId) " +
                    " from OrderTemplateMapping otm" +
                    " join Order o on o.id = otm.orderId" +
                    " join OrderUserMapping oum on oum.orderId = o.id " +
                    " join OrderUser ou on ou.id = oum.userId " +
                    " where otm.templateId = :templateId"
    )
    @EntityGraph(value = "Order_Graph", type = EntityGraph.EntityGraphType.FETCH)
    List<OrderWithParentId> getOrderInfos(Long templateId);
}
