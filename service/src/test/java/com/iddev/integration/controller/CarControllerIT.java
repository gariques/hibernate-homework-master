package com.iddev.integration.controller;

import com.iddev.dto.CarCreateEditDto;
import com.iddev.entity.Car;
import com.iddev.enums.CarStatus;
import com.iddev.integration.IntegrationTestBase;
import com.iddev.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

import static com.iddev.dto.CarCreateEditDto.Fields.colour;
import static com.iddev.dto.CarCreateEditDto.Fields.model;
import static com.iddev.dto.CarCreateEditDto.Fields.price;
import static com.iddev.dto.CarCreateEditDto.Fields.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@AutoConfigureMockMvc
@RequiredArgsConstructor
public class CarControllerIT extends IntegrationTestBase {

    private final MockMvc mockMvc;
    private final CarRepository carRepository;

    @Test
    void findAll() throws Exception {
        mockMvc.perform(get("/cars"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("car/cars"))
                .andExpect(model().attributeExists("cars"))
                .andExpect(model().attribute("cars", hasSize(4)));
    }

    @Test
    void findById() throws Exception {
        mockMvc.perform(get("/cars/1"))
                .andExpect(status().isOk());
    }

    @Test
    void create() throws Exception {
        mockMvc.perform(post("/cars")
                        .param(model, "testmodel")
                        .param(colour, "yellow")
                        .param(price, "3500")
                        .param(status, "AVAILABLE")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/cars/{\\d+}")
                );
    }

    @Test
    void update() throws Exception {
        mockMvc.perform(post("/cars/1/update")
                        .param(model, "testmodel")
                        .param(colour, "yellow")
                        .param(price, "3500")
                        .param(status, "AVAILABLE")
                )
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrlPattern("/cars/{\\d+}")
                );

        var expectedResult = Car.builder()
                .id(1L)
                .model("testmodel")
                .colour("yellow")
                .price(3500)
                .status(CarStatus.AVAILABLE)
                .build();
        var actualResult = carRepository.findById(1L);

        assertThat(actualResult).isPresent();
        assertEquals(expectedResult, actualResult.get());
    }

    @Test
    void delete() throws Exception {
        mockMvc.perform(post("/cars/1/delete"))
                .andExpectAll(
                        status().is3xxRedirection(),
                        redirectedUrl("/cars")
                );

        assertThat(carRepository.findById(1L).isEmpty()).isTrue();
    }
}
