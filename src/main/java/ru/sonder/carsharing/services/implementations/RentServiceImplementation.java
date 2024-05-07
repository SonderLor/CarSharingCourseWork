package ru.sonder.carsharing.services.implementations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.sonder.carsharing.DTOs.RentDTO;
import ru.sonder.carsharing.models.Car;
import ru.sonder.carsharing.models.Rent;
import ru.sonder.carsharing.models.User;
import ru.sonder.carsharing.repositories.CarRepository;
import ru.sonder.carsharing.repositories.RentRepository;
import ru.sonder.carsharing.repositories.UserRepository;
import ru.sonder.carsharing.services.RentService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Transactional
public class RentServiceImplementation implements RentService {
    private final RentRepository rentRepository;
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final List<String> addresses;

    @Autowired
    public RentServiceImplementation(RentRepository rentRepository, CarRepository carRepository, UserRepository userRepository) {
        this.rentRepository = rentRepository;
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        addresses = new ArrayList<>();
        addresses.add("ул. Тверская, д. 10");
        addresses.add("ул. Арбат, д. 15");
        addresses.add("ул. Пресненская набережная, д. 8");
        addresses.add("ул. Ленинский проспект, д. 25");
        addresses.add("ул. Новый Арбат, д. 20");
        addresses.add("ул. Таганская, д. 12");
        addresses.add("ул. Знаменка, д. 7");
        addresses.add("ул. Садовая-Кудринская, д. 30");
        addresses.add("ул. Красная Пресня, д. 5");
        addresses.add("ул. Моховая, д. 3");
        addresses.add("ул. Воздвиженка, д. 18");
        addresses.add("ул. Мясницкая, д. 22");
        addresses.add("ул. Пятницкая, д. 17");
        addresses.add("ул. Большая Дмитровка, д. 9");
        addresses.add("ул. Баррикадная, д. 11");
        addresses.add("ул. Солянка, д. 6");
        addresses.add("ул. Кузнецкий Мост, д. 2");
        addresses.add("ул. Большая Ордынка, д. 24");
        addresses.add("ул. Цветной бульвар, д. 13");
        addresses.add("ул. Пушкинская, д. 4");
    }

    public List<RentDTO> getRents() {
        return rentRepository.findAll().stream().map(Rent::toDto).toList();
    }

    public RentDTO getRent(Long id) {
        return rentRepository.findById(id).map(Rent::toDto).orElse(null);
    }

    public RentDTO getRentByCarIdAndUserId(Long carId, Long userId) {
        List<Rent> userRents = rentRepository.findByUser(userRepository.findById(userId).orElseThrow());

        Rent rent = userRents.stream()
                .filter(r -> r.getCar().getId().equals(carId))
                .findFirst().orElseThrow();

        return rent.toDto();
    }

    public RentDTO setRent(Long userId, Long carId) {
        User user = userRepository.findById(userId).orElseThrow();
        Car car = carRepository.findById(carId).orElseThrow();
        if (car.isAvailable()) {
            Rent rent = new Rent();
            rent.setUser(user);
            car.setAvailable(false);
            rent.setCar(car);
            rent.setStartDate(LocalDateTime.now());
            rent.setFinishDate(null);
            rent.setStartLocation(car.getLocation());
            rent.setFinishLocation(null);
            rentRepository.save(rent);
            return rent.toDto();
        }
        else {
            return null;
        }
    }

    public void finishRent(Long id) {
        Rent rent = rentRepository.findById(id).orElseThrow();
        User user = rent.getUser();
        String finishLocation = getRandomLocation();
        rent.getCar().setAvailable(true);
        rent.getCar().setLocation(finishLocation);
        rent.setFinishDate(LocalDateTime.now());
        rent.setFinishLocation(finishLocation);
        user.getRents().add(rent);
        if (user.getTotalTime() == null) {
            user.setTotalTime(Duration.between(rent.getStartDate(), rent.getFinishDate()).toSeconds());
        }
        else {
            user.setTotalTime(user.getTotalTime() + Duration.between(rent.getStartDate(), rent.getFinishDate()).toSeconds());
        }
    }

    public void deleteRent(Long rentId) {
        rentRepository.deleteById(rentId);
    }

    public void deleteRents() {
        rentRepository.deleteAll();
    }

    public List<RentDTO> getFilteredRents(String filteredBy, String value) {
        var entities = switch (filteredBy) {
            case "startLocation" -> rentRepository.findByStartLocationContains(value);
            case "finishLocation" -> rentRepository.findByFinishLocationContains(value);
            case "startDate" -> rentRepository.findByStartDateEquals(LocalDate.parse(value));
            case "finishDate" -> rentRepository.findByFinishDateEquals(LocalDate.parse(value));
            case "carId" -> rentRepository.findByCar(carRepository.findById(Long.valueOf(value)).orElseThrow());
            case "userId" -> rentRepository.findByUser(userRepository.findById(Long.valueOf(value)).orElseThrow());
            default -> throw new IllegalStateException("Unexpected value: " + filteredBy);
        };
        return entities.stream().map(Rent::toDto).toList();
    }

    private String getRandomLocation() {
        Random random = new Random();
        int index = random.nextInt(addresses.size());
        return addresses.get(index);
    }
}
