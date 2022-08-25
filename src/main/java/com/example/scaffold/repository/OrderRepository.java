package com.example.scaffold.repository;

import com.example.scaffold.model.master.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
