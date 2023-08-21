package com.iddev.integration.service;

import com.iddev.service.CarService;
import com.iddev.dto.CarCreateEditDto;
import com.iddev.enums.CarStatus;
import com.iddev.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiredArgsConstructor
public class CarServiceIT extends IntegrationTestBase {

    private static final Long LEXUS = 3L;

    private final CarService carService;

    @Test
    void findAll() {
        var result = carService.findAll();
        assertThat(result).hasSize(4);
    }

    @Test
    void findById() {
        var maybeCar = carService.findById(LEXUS);
        assertTrue(maybeCar.isPresent());
        maybeCar.ifPresent(car -> assertEquals("Lexus LX570", car.getModel()));
    }

    @Test
    void create() {
        CarCreateEditDto carDto = CarCreateEditDto.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var actualResult = carService.create(carDto);

        assertEquals(carDto.getModel(), actualResult.getModel());
        assertEquals(carDto.getColour(), actualResult.getColour());
        assertEquals(carDto.getPrice(), actualResult.getPrice());
        assertSame(carDto.getStatus(), actualResult.getStatus());
    }

    @Test
    void update() {
        CarCreateEditDto carDto = CarCreateEditDto.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();

        var actualResult = carService.update(LEXUS, carDto);

        assertTrue(actualResult.isPresent());

        actualResult.ifPresent(car -> {
            assertEquals(carDto.getModel(), car.getModel());
            assertEquals(carDto.getColour(), car.getColour());
            assertEquals(carDto.getPrice(), car.getPrice());
            assertSame(carDto.getStatus(), car.getStatus());
        });
    }

    @Test
    void delete() {
        assertFalse(carService.delete(999L));
        assertTrue(carService.delete(LEXUS));
    }
}
