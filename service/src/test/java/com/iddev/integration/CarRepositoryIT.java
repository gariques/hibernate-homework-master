package com.iddev.integration;

import com.iddev.annotation.IT;
import com.iddev.entity.Car;
import com.iddev.enums.CarStatus;
import com.iddev.filters.CarFilter;
import com.iddev.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
class CarRepositoryIT extends IntegrationTestBase {

    private final EntityManager entityManager;
    private final CarRepository carRepository;

    @Test
    void updateStatus() {
        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        carRepository.save(car);
        carRepository.updateStatus(CarStatus.NOT_AVAILABLE, car.getId());

        var updatedCar = carRepository.findById(car.getId());

        assertTrue(updatedCar.isPresent());
        assertEquals(CarStatus.NOT_AVAILABLE, updatedCar.get().getStatus());

    }

    @Test
    void saveCar() {
        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        carRepository.save(car);

        assertNotNull(car.getId());
    }

    @Test
    void readCar() {
        var expectedResult = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        carRepository.save(expectedResult);
        entityManager.clear();

        var actualResult = carRepository.findById(expectedResult.getId());

        assertNotNull(expectedResult.getId());
        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void deleteCar() {
        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        carRepository.save(car);
        entityManager.clear();
        carRepository.delete(car);

        assertThat(carRepository.findById(car.getId()).isEmpty()).isTrue();
    }

    @Test
    void getAvailableCars() {
        var carGraph = entityManager.createEntityGraph(Car.class);
        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var car2 = Car.builder()
                .model("Toyota Camry 70")
                .colour("black")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        var car3 = Car.builder()
                .model("Toyota Land Cruiser 300")
                .colour("white")
                .price(4000)
                .status(CarStatus.NOT_AVAILABLE)
                .build();

        var filter = CarFilter.builder()
                .status(CarStatus.AVAILABLE)
                .build();


        carRepository.save(car);
        carRepository.save(car2);
        carRepository.save(car3);

        List<Car> carList = carRepository.findAllAvailableCars(filter, carGraph);

        assertThat(carList).hasSize(2);
        assertThat(carList.get(0).getModel()).isEqualTo("Toyota Crown S220");
        assertThat(carList.get(1).getModel()).isEqualTo("Toyota Camry 70");
    }

    @Test
    void getCarsByColour() {
        var carGraph = entityManager.createEntityGraph(Car.class);
        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var car2 = Car.builder()
                .model("Toyota Camry 70")
                .colour("black")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        var car3 = Car.builder()
                .model("Toyota Land Cruiser 300")
                .colour("white")
                .price(4000)
                .status(CarStatus.NOT_AVAILABLE)
                .build();

        var filter = CarFilter.builder()
                .colour("white")
                .build();

        carRepository.save(car);
        carRepository.save(car2);
        carRepository.save(car3);

        List<Car> carList = carRepository.findCarsByColour(filter, carGraph);

        assertThat(carList).hasSize(2);
        assertThat(carList.get(0).getModel()).isEqualTo("Toyota Crown S220");
        assertThat(carList.get(1).getModel()).isEqualTo("Toyota Land Cruiser 300");
    }
}