package com.iddev.entity;

import com.iddev.enums.CarStatus;
import com.iddev.filters.CarFilter;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CarTest extends BaseTest {

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
        assertEquals(expectedResult, actualResult.get());
//        Денис, как тут правильно проверить, у меня метод гет подсвечивается
    }

    @Test
    void updateCar() {
        var car = Car.builder()
                .model("Toyota Crown S220")
                .colour("white")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        carRepository.save(car);
        car.setPrice(5000);
        car.setColour("new-Colour");
        entityManager.clear();

        carRepository.update(car);

        var updatedCar = carRepository.findById(car.getId());

        assertThat(updatedCar.get()).isEqualTo(car);
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

        List<Car> carList = carRepository.getAvailableCars(entityManager, filter, carGraph);

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

        List<Car> carList = carRepository.getCarsByColour(entityManager, filter, carGraph);

        assertThat(carList).hasSize(2);
        assertThat(carList.get(0).getModel()).isEqualTo("Toyota Crown S220");
        assertThat(carList.get(1).getModel()).isEqualTo("Toyota Land Cruiser 300");
    }
}