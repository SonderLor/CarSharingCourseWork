package ru.sonder.carsharing.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentDTO {
    private Long id;
    private Long userId;
    private Long carId;
    private LocalDateTime startDate;
    private LocalDateTime finishDate;
    private String startLocation;
    private String finishLocation;
}
