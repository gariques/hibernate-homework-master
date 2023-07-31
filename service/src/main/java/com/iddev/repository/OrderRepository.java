package com.iddev.repository;

import com.iddev.entity.Order;
import com.iddev.filters.ClientFilter;
import com.iddev.predicates.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.graph.GraphSemantic;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

import static com.iddev.entity.QClient.client;
import static com.iddev.entity.QOrder.order;

@Repository
public class OrderRepository extends AbstractCrudRepository<Long, Order> {


    public OrderRepository(EntityManager entityManager) {
        super(Order.class, entityManager);
    }

    public List<Order> getOrdersByFirstAndLastnames(EntityManager entityManager, ClientFilter filter, EntityGraph<Order> graph) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), client.firstName::eq)
                .add(filter.getLastName(), client.lastName::eq)
                .buildAnd();

        return new JPAQuery<Order>(entityManager)
                .select(order)
                .from(order)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .fetch();
    }
}
