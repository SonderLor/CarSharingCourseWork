package ru.sonder.carsharing.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.sonder.carsharing.DTOs.CarDTO;
import ru.sonder.carsharing.models.User;
import ru.sonder.carsharing.services.CarService;
import ru.sonder.carsharing.services.UserService;

import java.util.List;

@RequestMapping("/cars")
@Controller
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;
    private final UserService userService;

    @GetMapping
    public String getCars(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<CarDTO> cars = carService.getCars();
        model.addAttribute("cars", cars);
        User currentUser = userService.getUser(userDetails.getUsername());
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("userDetails", userDetails);
        return "cars";
    }

    @GetMapping("/{carId}")
    public String getCar(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("carId") Long carId, Model model) {
        CarDTO carDTO = carService.getCar(carId);
        model.addAttribute("car", carDTO);
        model.addAttribute("userDetails", userDetails);
        return "car";
    }

    @GetMapping("/sorted")
    public String getCarsSortedByYear(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        List<CarDTO> cars = carService.getCarsSortedByYear();
        model.addAttribute("cars", cars);
        User currentUser = userService.getUser(userDetails.getUsername());
        model.addAttribute("userId", currentUser.getId());
        model.addAttribute("userDetails", userDetails);
        return "cars";
    }

    @GetMapping("/filtered")
    public String getFilteredCars(@RequestParam String filteredBy, @RequestParam String value, Model model) {
        List<CarDTO> filteredCars = carService.getFilteredCars(filteredBy, value);
        model.addAttribute("cars", filteredCars);
        return "cars";
    }
}
