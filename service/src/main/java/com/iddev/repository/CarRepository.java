package com.iddev.repository;

import com.iddev.entity.Car;
import com.iddev.filters.CarFilter;
import com.iddev.predicates.QPredicate;
import com.querydsl.jpa.impl.JPAQuery;
import org.hibernate.Session;
import org.hibernate.graph.GraphSemantic;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

import static com.iddev.entity.QCar.car;

public class CarRepository extends AbstractCrudRepository<Long, Car> {

    public CarRepository(EntityManager entityManager) {
        super(Car.class, entityManager);
    }

    public List<Car> getAvailableCars(EntityManager entityManager, CarFilter filter, EntityGraph<Car> graph) {
        var predicate = QPredicate.builder()
                .add(filter.getStatus(), car.status::eq)
                .add(filter.getStatus(), car.status::eq)
                .buildAnd();

        return new JPAQuery<Car>(entityManager)
                .select(car)
                .from(car)
                .where(predicate)
                .setHint(GraphSemantic.FETCH.getJpaHintName(), graph)
                .fetch();
    }

    public List<Car> getCarsByColour(EntityManager entityManager, CarFilter filter, EntityGraph<Car> graph) {
        var predicate = QPredicate.builder()
                .add(filter.getColour(), car.colour::eq)
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
