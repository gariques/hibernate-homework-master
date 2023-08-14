package com.iddev.repository;

import com.iddev.entity.Order;

import org.springframework.data.jpa.repository.JpaRepository;


public interface OrderRepository extends JpaRepository<Order, Long>, FilterOrderRepository {

}
