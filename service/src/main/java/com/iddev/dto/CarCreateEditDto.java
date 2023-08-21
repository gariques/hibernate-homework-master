package com.iddev.dto;

import com.iddev.enums.CarStatus;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;

@Value
@FieldNameConstants
@Builder
public class CarCreateEditDto {

    String model;
    String colour;
    Integer price;
    CarStatus status;
}
