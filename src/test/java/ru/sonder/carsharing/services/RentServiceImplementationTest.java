package ru.sonder.carsharing.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.sonder.carsharing.DTOs.RentDTO;
import ru.sonder.carsharing.models.Car;
import ru.sonder.carsharing.models.Rent;
import ru.sonder.carsharing.models.User;
import ru.sonder.carsharing.repositories.CarRepository;
import ru.sonder.carsharing.repositories.RentRepository;
import ru.sonder.carsharing.repositories.UserRepository;
import ru.sonder.carsharing.services.implementations.RentServiceImplementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServiceImplementationTest {

    @Mock
    private RentRepository rentRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentServiceImplementation rentService;

    private User testUser;
    private Car testCar;
    private Rent testRent;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUser");

        testCar = new Car();
        testCar.setId(1L);
        testCar.setAvailable(true);
        testCar.setLocation("Test Location");

        testRent = new Rent();
        testRent.setId(1L);
        testRent.setUser(testUser);
        testRent.setCar(testCar);
        testRent.setStartDate(LocalDateTime.now());
        testRent.setFinishDate(null);
        testRent.setStartLocation("Test Location");
        testRent.setFinishLocation(null);
    }

    @Test
    void testGetRents() {
        List<Rent> rents = new ArrayList<>();
        rents.add(testRent);

        when(rentRepository.findAll()).thenReturn(rents);

        List<RentDTO> rentDTOs = rentService.getRents();

        assertEquals(1, rentDTOs.size());
        assertEquals(testRent.getId(), rentDTOs.get(0).getId());
    }

    @Test
    void testGetRent() {
        when(rentRepository.findById(testRent.getId())).thenReturn(java.util.Optional.ofNullable(testRent));

        RentDTO rentDTO = rentService.getRent(testRent.getId());

        assertNotNull(rentDTO);
        assertEquals(testRent.getId(), rentDTO.getId());
    }

    @Test
    void testSetRent() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.ofNullable(testUser));
        when(carRepository.findById(testCar.getId())).thenReturn(java.util.Optional.ofNullable(testCar));

        RentDTO rentDTO = rentService.setRent(testUser.getId(), testCar.getId());

        assertNotNull(rentDTO);
        assertEquals(testCar.getLocation(), rentDTO.getStartLocation());
        assertFalse(testCar.isAvailable());
    }

    @Test
    void testFinishRent() {
        testRent.setFinishDate(LocalDateTime.now());
        testCar.setAvailable(false);
        testCar.setLocation("Test Finish Location");
        when(rentRepository.findById(testRent.getId())).thenReturn(java.util.Optional.ofNullable(testRent));

        rentService.finishRent(testRent.getId());

        assertNotNull(testRent.getFinishDate());
        assertTrue(testCar.isAvailable());
    }

    @Test
    void testDeleteRent() {
        doNothing().when(rentRepository).deleteById(testRent.getId());

        rentService.deleteRent(testRent.getId());

        verify(rentRepository, times(1)).deleteById(testRent.getId());
    }

    @Test
    void testDeleteRents() {
        doNothing().when(rentRepository).deleteAll();

        rentService.deleteRents();

        verify(rentRepository, times(1)).deleteAll();
    }

    @Test
    void testGetRentByCarIdAndUserId() {
        when(userRepository.findById(testUser.getId())).thenReturn(java.util.Optional.ofNullable(testUser));
        when(rentRepository.findByUser(testUser)).thenReturn(List.of(testRent));

        RentDTO rentDTO = rentService.getRentByCarIdAndUserId(testCar.getId(), testUser.getId());

        assertNotNull(rentDTO);
        assertEquals(testRent.getId(), rentDTO.getId());
    }

    @Test
    void testGetFilteredRents() {
        when(rentRepository.findByStartLocationContains("Test Location")).thenReturn(List.of(testRent));

        List<RentDTO> rentDTOs = rentService.getFilteredRents("startLocation", "Test Location");

        assertNotNull(rentDTOs);
        assertEquals(1, rentDTOs.size());
        assertEquals(testRent.getId(), rentDTOs.get(0).getId());
    }
}
