package com.iddev.repository;

import com.iddev.entity.Client;
import com.iddev.filters.ClientFilter;
import com.iddev.predicates.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

import static com.iddev.entity.QClient.client;

public class ClientRepository extends AbstractCrudRepository<Long, Client> {

    public ClientRepository(EntityManager entityManager) {
        super(Client.class, entityManager);
    }

    public List<Client> getClientsByFirstAndLastnames(EntityManager entityManager, ClientFilter filter, EntityGraph<Client> graph) {
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

    public List<Client> findAllByFirstAndLastnames(Session session, String firstName, String lastName) {
        return new JPAQuery<Client>(session)
                .select(client)
                .from(client)
                .where(client.firstName.eq(firstName)
                        .and(client.lastName.eq(lastName)))
                .fetch();
    }
}
