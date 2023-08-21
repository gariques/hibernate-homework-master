package com.iddev.unit.service;

import com.iddev.service.CarService;
import com.iddev.dto.CarCreateEditDto;
import com.iddev.dto.CarReadDto;
import com.iddev.entity.Car;
import com.iddev.enums.CarStatus;
import com.iddev.mapper.CarCreateEditMapper;
import com.iddev.mapper.CarReadMapper;
import com.iddev.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;
    @Mock
    private CarReadMapper carReadMapper;
    @Mock
    private CarCreateEditMapper carCreateEditMapper;
    @InjectMocks
    private CarService carService;

    @Test
    void findAll() {
        var car = Car.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var car2 = Car.builder()
                .model("testmodel2")
                .colour("white")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        var carReadDto = CarReadDto.builder()
                .id(1L)
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var carReadDto2 = CarReadDto.builder()
                .id(1L)
                .model("testmodel2")
                .colour("white")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        var expectedResult = Arrays.asList(car, car2);
        doReturn(expectedResult).when(carRepository).findAll();
        doReturn(carReadDto).when(carReadMapper).map(car);
        doReturn(carReadDto2).when(carReadMapper).map(car2);

        var actualResult = carService.findAll();

        assertEquals(2, actualResult.size());
        verify(carRepository).findAll();
    }

    @Test
    void findById() {
        var car = Car.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var expectedResult = CarReadDto.builder()
                .id(1L)
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        doReturn(Optional.of(car)).when(carRepository).findById(1L);
        doReturn(expectedResult).when(carReadMapper).map(car);

        var actualResult = carService.findById(1L);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
        verify(carRepository).findById(1L);
    }

    @Test
    void create() {
        CarCreateEditDto carCreateEditDto = CarCreateEditDto.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var car = Car.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var expectedResult = CarReadDto.builder()
                .id(1L)
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        doReturn(car).when(carCreateEditMapper).map(carCreateEditDto);
        doReturn(car).when(carRepository).save(car);
        doReturn(expectedResult).when(carReadMapper).map(car);

        var actualResult = carService.create(carCreateEditDto);

        assertThat(actualResult).isEqualTo(expectedResult);
        verify(carRepository).save(car);
    }

    @Test
    void update() {
        CarCreateEditDto carCreateEditDto = CarCreateEditDto.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var car = Car.builder()
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        var updatedCar = Car.builder()
                .model("testmodel2")
                .colour("white")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        var expectedResult = CarReadDto.builder()
                .id(1L)
                .model("testmodel2")
                .colour("white")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        doReturn(Optional.of(car)).when(carRepository).findById(1L);
        doReturn(updatedCar).when(carCreateEditMapper).map(carCreateEditDto, car);
        doReturn(updatedCar).when(carRepository).saveAndFlush(updatedCar);
        doReturn(expectedResult).when(carReadMapper).map(updatedCar);

        var actualResult = carService.update(1L, carCreateEditDto);

        assertTrue(actualResult.isPresent());
        assertEquals(expectedResult, actualResult.get());
        verify(carRepository).saveAndFlush(updatedCar);
    }

    @Test
    void delete() {
        var car = Car.builder()
                .id(1L)
                .model("testmodel")
                .colour("black")
                .price(3000)
                .status(CarStatus.AVAILABLE)
                .build();
        doReturn(Optional.of(car)).when(carRepository).findById(1L);

        var actualResult = carService.delete(1L);

        assertTrue(actualResult);
        verify(carRepository).findById(1L);
        verify(carRepository).delete(car);
        verify(carRepository).flush();
    }

    @Test
    void deleteFailed() {
        doReturn(Optional.empty()).when(carRepository).findById(1L);

        var actualResult = carService.delete(1L);

        assertFalse(actualResult);
        verify(carRepository).findById(1L);
        verifyNoMoreInteractions(carRepository);
    }
}