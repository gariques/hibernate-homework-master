package com.iddev.mapper;

import com.iddev.dto.CarReadDto;
import com.iddev.entity.Car;
import org.springframework.stereotype.Component;

@Component
public class CarReadMapper implements Mapper<Car, CarReadDto> {

    @Override
    public CarReadDto map(Car object) {
        return CarReadDto.builder()
                .id(object.getId())
                .model(object.getModel())
                .colour(object.getColour())
                .price(object.getPrice())
                .status(object.getStatus())
                .build();
    }
}

