package com.iddev.mapper;

import com.iddev.dto.CarCreateEditDto;
import com.iddev.entity.Car;
import org.springframework.stereotype.Component;

@Component
public class CarCreateEditMapper implements Mapper<CarCreateEditDto, Car> {

    @Override
    public Car map(CarCreateEditDto fromObject, Car toObject) {
        copy(fromObject, toObject);
        return toObject;
    }

    @Override
    public Car map(CarCreateEditDto object) {
        var car = new Car();
        copy(object, car);

        return car;
    }

    private void copy(CarCreateEditDto object, Car car) {
        car.setModel(object.getModel());
        car.setColour(object.getColour());
        car.setPrice(object.getPrice());
        car.setStatus(object.getStatus());
    }
}
