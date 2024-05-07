package ru.sonder.carsharing.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sonder.carsharing.models.Rent;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarDTO {
    private Long id;
    private String brand;
    private String model;
    private int year;
    private String color;
    private boolean available;
    private String location;
    private List<Rent> rents = new ArrayList<>();
}
