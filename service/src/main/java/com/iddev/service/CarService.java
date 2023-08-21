package com.iddev.service;

import com.iddev.dto.CarCreateEditDto;
import com.iddev.dto.CarReadDto;
import com.iddev.mapper.CarCreateEditMapper;
import com.iddev.mapper.CarReadMapper;
import com.iddev.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CarService {

    private final CarRepository carRepository;
    private final CarReadMapper carReadMapper;
    private final CarCreateEditMapper carCreateEditMapper;

    public List<CarReadDto> findAll() {
        return carRepository.findAll().stream()
                .map(carReadMapper::map)
                .toList();
    }

    public Optional<CarReadDto> findById(Long id) {
        return carRepository.findById(id)
                .map(carReadMapper::map);
    }

    @Transactional
    public CarReadDto create(CarCreateEditDto carDto) {
        return Optional.of(carDto)
                .map(carCreateEditMapper::map)
                .map(carRepository::save)
                .map(carReadMapper::map)
                .orElseThrow();
    }

    @Transactional
    public Optional<CarReadDto> update(Long id, CarCreateEditDto carDto) {
        return carRepository.findById(id)
                .map(entity -> carCreateEditMapper.map(carDto, entity))
                .map(carRepository::saveAndFlush)
                .map(carReadMapper::map);
    }

    @Transactional
    public boolean delete(Long id) {
        return carRepository.findById(id)
                .map(entity -> {
                    carRepository.delete(entity);
                    carRepository.flush();
                    return true;
                })
                .orElse(false);
    }
}