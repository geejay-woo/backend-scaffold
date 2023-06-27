package com.example.scaffold.controller;

import com.example.scaffold.domain.OrderWithParentId;
import com.example.scaffold.model.master.Project;
import com.example.scaffold.model.master.ProjectMember;
import com.example.scaffold.repository.ProjectMemberRepository;
import com.example.scaffold.repository.ProjectRepository;
import com.example.scaffold.request.SaveOrderRequest;
import com.example.scaffold.response.OrderDetailsResponse;
import com.example.scaffold.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final EntityManager entityManager;
    private final ProjectMemberRepository projectMemberRepository;
    private final ProjectRepository projectRepository;

    @GetMapping("/test")
    public ResponseEntity<List<OrderWithParentId>> getOrderDetails2() {
        List<OrderWithParentId> orderDetailsResponse = orderService.getOrderInfo();
        return ResponseEntity.ok(orderDetailsResponse);
    }

    @GetMapping("/{orderId}")
    @ApiOperation(value = "获取订单详情", httpMethod = "GET", response = OrderDetailsResponse.class)
    public ResponseEntity<OrderDetailsResponse> getOrderDetails(@PathVariable("orderId") Long orderId) {
        OrderDetailsResponse orderDetailsResponse = orderService.getOrderByOrderId(orderId);
        return ResponseEntity.ok(orderDetailsResponse);
    }

    @PostMapping
    @ApiOperation(value = "保存订单信息", httpMethod = "POST")
    public ResponseEntity postOrder(@RequestBody @Valid SaveOrderRequest request) {
        orderService.saveOrder(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity get() {
        projectMemberRepository.save(ProjectMember.builder()
                        .projectCode("T2222")
                        .roleName("普通用户")
                        .userId("10002")
                        .project(Project.builder()
                                .projectCode("T2222").projectType("普通类").build())
                .build());

//        Specification<Project> specification = (root, query, cb) ->  {
//            Predicate p1 = cb.equal(root.get("projectCode"), "T1111");
//            Predicate p2 = null;
//            query.where(p1,p2);
//            return query.getRestriction();
////            return cb.equal(root.get("projectCode"), "T1111");
//        };
//        List<Project> all = projectRepository.findAll(specification);

//        List<ProjectMember> all = projectMemberRepository.findAll();
//        List<Project> all1 = projectRepository.findAll();
//        System.out.println(all);
//        System.out.println(all1);
        return ResponseEntity.ok().build();
    }




}
