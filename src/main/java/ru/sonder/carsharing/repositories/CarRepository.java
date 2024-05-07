package ru.sonder.carsharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.sonder.carsharing.models.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findCarByBrandEquals(String brand);
    List<Car> findCarByModelEquals(String model);
    List<Car> findCarByYearEquals(Integer year);
    List<Car> findCarByColorEquals(String color);
    List<Car> findCarByAvailableEquals(Boolean available);
    List<Car> findCarByLocationEquals(String location);
}
