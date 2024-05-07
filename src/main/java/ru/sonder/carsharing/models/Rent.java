package ru.sonder.carsharing.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.sonder.carsharing.DTOs.RentDTO;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rents")
public class Rent {
    @Id
    @SequenceGenerator(name = "rents_seq", sequenceName = "rents_sequence", allocationSize = 1)
    @GeneratedValue(generator = "rents_seq", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "finish_date")
    private LocalDateTime finishDate;

    @Column(name = "start_location")
    private String startLocation;

    @Column(name = "finish_location")
    private String finishLocation;

    @JsonIgnore
    @ManyToOne
    private User user;

    @JsonIgnore
    @ManyToOne
    private Car car;

    public long getRentDurationInSeconds() {
        if (startDate != null && finishDate != null) {
            return Duration.between(startDate, finishDate).getSeconds();
        }
        if (startDate != null) {
            return Duration.between(startDate, LocalDateTime.now()).getSeconds();
        }
        return 0;
    }

    public String getFormattedStartDate() {
        if (startDate != null) {
            return startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        return null;
    }

    public String getFormattedFinishDate() {
        if (finishDate != null) {
            return finishDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        return null;
    }

    public RentDTO toDto() {
        return new RentDTO(id, user.getId(), car.getId(), startDate, finishDate, startLocation, finishLocation);
    }
}
