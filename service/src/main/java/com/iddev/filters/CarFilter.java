package com.iddev.filters;

import com.iddev.enums.CarStatus;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CarFilter {

    CarStatus status;
    String colour;
    Integer price;
}
