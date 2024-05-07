package ru.sonder.carsharing.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import ru.sonder.carsharing.DTOs.CarDTO;
import ru.sonder.carsharing.models.Car;
import ru.sonder.carsharing.repositories.CarRepository;
import ru.sonder.carsharing.services.implementations.CarServiceImplementation;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplementationTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarServiceImplementation carService;

    private CarDTO testCarDTO;
    private Car testCar;

    @BeforeEach
    void setUp() {
        testCarDTO = new CarDTO();
        testCarDTO.setBrand("TestBrand");
        testCarDTO.setModel("TestModel");
        testCarDTO.setYear(2020);
        testCarDTO.setColor("TestColor");
        testCarDTO.setAvailable(true);
        testCarDTO.setLocation("TestLocation");

        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("TestBrand");
        testCar.setModel("TestModel");
        testCar.setYear(2020);
        testCar.setColor("TestColor");
        testCar.setAvailable(true);
        testCar.setLocation("TestLocation");
    }

    @Test
    void testGetCars() {
        List<Car> cars = new ArrayList<>();
        cars.add(testCar);

        when(carRepository.findAll()).thenReturn(cars);

        List<CarDTO> carDTOs = carService.getCars();

        assertEquals(1, carDTOs.size());
        assertEquals(testCar.getId(), carDTOs.get(0).getId());
    }

    @Test
    void testGetCar() {
        when(carRepository.findById(testCar.getId())).thenReturn(java.util.Optional.ofNullable(testCar));

        CarDTO carDTO = carService.getCar(testCar.getId());

        assertNotNull(carDTO);
        assertEquals(testCar.getId(), carDTO.getId());
    }

    @Test
    void testSetCar() {
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        CarDTO savedCarDTO = carService.setCar(testCarDTO);

        assertNotNull(savedCarDTO);
    }

    @Test
    void testSetCars() {
        List<CarDTO> carDTOs = new ArrayList<>();
        carDTOs.add(testCarDTO);

        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        List<CarDTO> savedCarDTOs = carService.setCars(carDTOs);

        assertNotNull(savedCarDTOs);
        assertEquals(1, savedCarDTOs.size());
    }

    @Test
    void testDeleteCar() {
        doNothing().when(carRepository).deleteById(testCar.getId());

        carService.deleteCar(testCar.getId());

        verify(carRepository, times(1)).deleteById(testCar.getId());
    }

    @Test
    void testDeleteCars() {
        doNothing().when(carRepository).deleteAll();

        carService.deleteCars();

        verify(carRepository, times(1)).deleteAll();
    }

    @Test
    void testGetCarsSortedByYear() {
        List<Car> cars = new ArrayList<>();
        cars.add(testCar);

        when(carRepository.findAll(any(Sort.class))).thenReturn(cars);

        List<CarDTO> carDTOs = carService.getCarsSortedByYear();

        assertEquals(1, carDTOs.size());
        assertEquals(testCar.getId(), carDTOs.get(0).getId());
    }

    @Test
    void testGetFilteredCars() {
        when(carRepository.findCarByBrandEquals("TestBrand")).thenReturn(List.of(testCar));

        List<CarDTO> carDTOs = carService.getFilteredCars("brand", "TestBrand");

        assertNotNull(carDTOs);
        assertEquals(1, carDTOs.size());
        assertEquals(testCar.getId(), carDTOs.get(0).getId());
    }
}
