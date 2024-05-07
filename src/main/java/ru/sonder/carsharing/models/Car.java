package ru.sonder.carsharing.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sonder.carsharing.DTOs.CarDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cars")
public class Car {
    @Id
    @SequenceGenerator(name = "cars_seq", sequenceName = "cars_sequence", allocationSize = 1)
    @GeneratedValue(generator = "cars_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "year")
    private int year;

    @Column(name = "color")
    private String color;

    @Column(name = "available")
    private boolean available;

    @Column(name = "location")
    private String location;

    @OneToMany(mappedBy = "car", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rent> rents = new ArrayList<>();

    public CarDTO toDto() {
        return new CarDTO(id, brand, model, year, color, available, location, rents);
    }
}
