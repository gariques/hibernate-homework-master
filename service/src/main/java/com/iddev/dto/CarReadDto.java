package com.iddev.dto;

import com.iddev.enums.CarStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarReadDto {

    Long id;
    String model;
    String colour;
    Integer price;
    CarStatus status;
}
