package ru.sonder.carsharing.services;

import ru.sonder.carsharing.DTOs.CarDTO;

import java.util.List;

public interface CarService {
    List<CarDTO> getCars();
    CarDTO getCar(Long id);
    CarDTO setCar(CarDTO carDTO);
    List<CarDTO> setCars(List<CarDTO> carDTOs);
    void deleteCar(Long carId);
    void deleteCars();
    List<CarDTO> getCarsSortedByYear();
    List<CarDTO> getFilteredCars(String filteredBy, String value);
}
