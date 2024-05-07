package ru.sonder.carsharing.services.implementations;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sonder.carsharing.DTOs.CarDTO;
import ru.sonder.carsharing.models.Car;
import ru.sonder.carsharing.repositories.CarRepository;
import ru.sonder.carsharing.services.CarService;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CarServiceImplementation implements CarService {
    private final CarRepository carRepository;

    public List<CarDTO> getCars() {
        return carRepository.findAll().stream().map(Car::toDto).toList();
    }

    public CarDTO getCar(Long id) {
        return carRepository.findById(id).map(Car::toDto).orElse(null);
    }

    public CarDTO setCar(CarDTO carDTO) {
        Car car = new Car();
        car.setBrand(carDTO.getBrand());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setColor(carDTO.getColor());
        car.setAvailable(carDTO.isAvailable());
        car.setLocation(carDTO.getLocation());
        carRepository.save(car);
        return car.toDto();
    }

    public List<CarDTO> setCars(List<CarDTO> carDTOs) {
        List<CarDTO> savedCarDTOs = new ArrayList<>();
        for (CarDTO userDTO : carDTOs) {
            CarDTO user = setCar(userDTO);
            savedCarDTOs.add(user);
        }
        return savedCarDTOs;
    }

    public void deleteCar(Long carId) {
        carRepository.deleteById(carId);
    }

    public void deleteCars() {
        carRepository.deleteAll();
    }

    public List<CarDTO> getCarsSortedByYear() {
        return carRepository.findAll(Sort.by(Sort.Direction.ASC, "year"))
                .stream().map(Car::toDto).toList();
    }

    public List<CarDTO> getFilteredCars(String filteredBy, String value) {
        var entities = switch (filteredBy) {
            case "brand" -> carRepository.findCarByBrandEquals(value);
            case "model" -> carRepository.findCarByModelEquals(value);
            case "year" -> carRepository.findCarByYearEquals(Integer.parseInt(value));
            case "color" -> carRepository.findCarByColorEquals(value);
            case "available" -> carRepository.findCarByAvailableEquals(Boolean.parseBoolean(value));
            case "location" -> carRepository.findCarByLocationEquals(value);
            default -> throw new IllegalStateException("Unexpected value: " + filteredBy);
        };
        return entities.stream().map(Car::toDto).toList();
    }
}
