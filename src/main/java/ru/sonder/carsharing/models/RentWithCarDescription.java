package ru.sonder.carsharing.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentWithCarDescription {
    private Rent rent;
    private String carDescription;
}

