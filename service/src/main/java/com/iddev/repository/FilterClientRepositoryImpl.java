package com.iddev.repository;

import com.iddev.entity.Client;
import com.iddev.filters.ClientFilter;
import com.iddev.predicates.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

import static com.iddev.entity.QClient.client;

@RequiredArgsConstructor
public class FilterClientRepositoryImpl implements FilterClientRepository{

    private final EntityManager entityManager;

    @Override
    public List<Client> findClientsByFirstAndLastnames(EntityManager entityManager, ClientFilter filter, EntityGraph<Client> graph) {
        var predicate = QPredicate.builder()
                .add(filter.getFirstName(), client.firstName::eq)
                .add(filter.getLastName(), client.lastName::eq)
                .buildAnd();

        return new JPAQuery<Client>(entityManager)
                .select(client)
                .from(client)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .fetch();
    }
}
