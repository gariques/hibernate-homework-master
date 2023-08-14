package com.iddev.repository;

import com.iddev.entity.Order;
import com.iddev.filters.ClientFilter;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

public interface FilterOrderRepository {

    List<Order> findOrdersByFirstAndLastnames(EntityManager entityManager, ClientFilter filter, EntityGraph<Order> graph);
}
