package com.iddev.repository;

import com.iddev.entity.Car;
import com.iddev.filters.CarFilter;
import com.iddev.predicates.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

import static com.iddev.entity.QCar.car;

@RequiredArgsConstructor
public class FilterCarRepositoryImpl implements FilterCarRepository {

    private final EntityManager entityManager;

    @Override
    public List<Car> findAllAvailableCars(CarFilter filter, EntityGraph<Car> graph) {
        var predicate = QPredicate.builder()
                .add(filter.getStatus(), car.status::eq)
                .buildAnd();

        return new JPAQuery<Car>(entityManager)
                .select(car)
                .from(car)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .fetch();
    }

    @Override
    public List<Car> findCarsByColour(CarFilter filter, EntityGraph<Car> graph) {
        var predicate = QPredicate.builder()
                .add(filter.getColour(), car.colour::eq)
                .buildAnd();

        return new JPAQuery<Car>(entityManager)
                .select(car)
                .from(car)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .fetch();
    }
}
