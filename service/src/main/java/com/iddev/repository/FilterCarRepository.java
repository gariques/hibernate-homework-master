package com.iddev.repository;

import com.iddev.entity.Car;
import com.iddev.filters.CarFilter;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import java.util.List;

public interface FilterCarRepository {

    List<Car> findAllAvailableCars(CarFilter filter, EntityGraph<Car> graph);

    List<Car> findCarsByColour(CarFilter filter, EntityGraph<Car> graph);
}
