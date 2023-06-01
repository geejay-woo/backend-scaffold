package com.example.scaffold.repository;

import com.example.scaffold.domain.OrderWithParentId;
import com.example.scaffold.model.master.OrderTemplateMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderTemplateMappingRepository extends JpaRepository<OrderTemplateMapping, Long> {

    @Query(
            "select new com.example.scaffold.domain.OrderWithParentId(o, otm.parentOrderId) " +
                    " from OrderTemplateMapping otm" +
                    " join Order o on o.id = otm.orderId" +
                    " where otm.templateId = :templateId"
    )
    List<OrderWithParentId> getOrderInfos(Long templateId);

}
