package com.iddev.repository;

import com.iddev.entity.Client;
import com.iddev.filters.ClientFilter;
import org.hibernate.Session;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

public interface FilterClientRepository {

    List<Client> findClientsByFirstAndLastnames(EntityManager entityManager, ClientFilter filter, EntityGraph<Client> graph);

}
