package ru.sonder.carsharing.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.sonder.carsharing.models.Car;
import ru.sonder.carsharing.models.Rent;
import ru.sonder.carsharing.models.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RentRepository extends JpaRepository<Rent, Long> {
    List<Rent> findByStartLocationContains(String value);
    List<Rent> findByFinishLocationContains(String value);
    List<Rent> findByStartDateEquals(LocalDate value);
    List<Rent> findByFinishDateEquals(LocalDate value);
    List<Rent> findByCar(Car car);
    List<Rent> findByUser(User user);
}
