package ru.sonder.carsharing.services;

import ru.sonder.carsharing.DTOs.RentDTO;

import java.util.List;

public interface RentService {
    List<RentDTO> getRents();
    RentDTO getRent(Long id);
    RentDTO setRent(Long userId, Long carId);
    void finishRent(Long id);
    void deleteRent(Long rentId);
    void deleteRents();
    RentDTO getRentByCarIdAndUserId(Long carId, Long userId);
    List<RentDTO> getFilteredRents(String filteredBy, String value);
}
