package com.iddev.repository;

import com.iddev.entity.Car;
import com.iddev.enums.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CarRepository extends JpaRepository<Car, Long>, FilterCarRepository {

    @Modifying(clearAutomatically = true)
    @Query("update Car c " +
            "set c.status = :status " +
            "where c.id = :id")
    void updateStatus(CarStatus status, Long id);
}
